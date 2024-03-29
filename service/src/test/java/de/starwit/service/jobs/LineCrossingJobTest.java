package de.starwit.service.jobs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.starwit.persistence.analytics.entity.Direction;
import de.starwit.persistence.databackend.entity.ObservationJobEntity;
import de.starwit.persistence.databackend.entity.JobType;
import de.starwit.persistence.sae.entity.SaeDetectionEntity;
import de.starwit.persistence.sae.repository.SaeDao;
import de.starwit.service.analytics.LineCrossingService;

@ExtendWith(MockitoExtension.class)
public class LineCrossingJobTest {

    @Mock
    SaeDao saeDaoMock;

    @Mock
    LineCrossingService serviceMock;
    
    @Test
    public void testLineCrossing() throws InterruptedException {

        ObservationJobEntity entity = prepareJobEntity();
        JobData<SaeDetectionEntity> jobData = new JobData<>(entity);
        
        // No point on trajectory should be ON the counting line (b/c direction is then ambiguous)
        List<SaeDetectionEntity> detections = createLinearTrajectory(
            new Point2D.Double(50, 55), new Point2D.Double(50, 155), 
            10, Duration.ofMillis(250));
        when(saeDaoMock.getDetectionData(any(), any(), any())).thenReturn(detections);
        
        LineCrossingJob testee = new LineCrossingJob(saeDaoMock, serviceMock);

        testee.run(jobData);
        
        ArgumentCaptor<Direction> directionCaptor = ArgumentCaptor.forClass(Direction.class);
        verify(serviceMock, times(1)).addEntry(any(), directionCaptor.capture(), any());

        assertThat(directionCaptor.getValue()).isEqualTo(Direction.out);
    }

    static ObservationJobEntity prepareJobEntity() {
        ObservationJobEntity entity = new ObservationJobEntity();
        entity.setCameraId("camId");
        entity.setDetectionClassId(1);
        entity.setGeometryPoints(Arrays.asList(
            Helper.createPoint(0, 100), 
            Helper.createPoint(100, 100)
        ));
        entity.setType(JobType.LINE_CROSSING);
        entity.setGeoReferenced(false);

        return entity;
    }

    static List<SaeDetectionEntity> createLinearTrajectory(Point2D start, Point2D end, int numSteps, Duration stepInterval) {
        List<SaeDetectionEntity> trajectory = new ArrayList<>();

        Instant startTime = Instant.now().minusSeconds(100);

        trajectory.add(Helper.createDetection(startTime, new Point2D.Double(start.getX(), start.getY())));
        double currentX = start.getX();
        double currentY = start.getY();
        
        for (int i = 0; i < numSteps; i++) {
            currentX += (end.getX() - start.getX()) / numSteps;
            currentY += (end.getY() - start.getY()) / numSteps;
            trajectory.add(Helper.createDetection(startTime.plus(stepInterval.multipliedBy(i+1)), new Point2D.Double(currentX, currentY)));
        }

        return trajectory;
    }

}
