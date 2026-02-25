package com.spiga.app;

import com.spiga.core.ActifMobile;
import com.spiga.core.Point3D;
import com.spiga.mission.Mission;
import com.spiga.mission.ObjectifMission;
import com.spiga.mission.StatutMission;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimulationBootstrapTest {

    private SimulationBootstrap bootstrap;
    private SimulationContext context;

    @BeforeEach
    public void setUp() {
        bootstrap = new SimulationBootstrap();
        context = bootstrap.createDemoContext();
    }

    @Test
    public void testAssignGlobalTargetSkipsIncompatibleAssets() {
        Point3D waterTarget = new Point3D(100, 100, 20);
        bootstrap.assignGlobalTarget(context, waterTarget);

        ActifMobile drone = context.getGestionnaire().getActifParId("D1");
        ActifMobile car = context.getGestionnaire().getActifParId("C1");
        ActifMobile ship = context.getGestionnaire().getActifParId("S1");
        ActifMobile submarine = context.getGestionnaire().getActifParId("U1");

        assertNotNull(drone.getTarget());
        assertEquals(20, drone.getTarget().getZ(), 1e-6);

        assertNull(car.getTarget());

        assertNotNull(ship.getTarget());
        assertEquals(0, ship.getTarget().getZ(), 1e-6);

        assertNotNull(submarine.getTarget());
        assertEquals(0, submarine.getTarget().getZ(), 1e-6);
    }

    @Test
    public void testAssignGlobalTargetSkipsMarineOnLand() {
        Point3D landTarget = new Point3D(300, 300, 10);
        bootstrap.assignGlobalTarget(context, landTarget);

        ActifMobile drone = context.getGestionnaire().getActifParId("D1");
        ActifMobile car = context.getGestionnaire().getActifParId("C1");
        ActifMobile ship = context.getGestionnaire().getActifParId("S1");
        ActifMobile submarine = context.getGestionnaire().getActifParId("U1");

        assertNotNull(drone.getTarget());
        assertEquals(10, drone.getTarget().getZ(), 1e-6);

        assertNotNull(car.getTarget());
        assertEquals(0, car.getTarget().getZ(), 1e-6);

        assertNull(ship.getTarget());
        assertNull(submarine.getTarget());
    }

    @Test
    public void testCreateGroupMissionSkipsIncompatibleAssets() {
        Point3D waterTarget = new Point3D(100, 100, 0);
        Mission mission = bootstrap.createGroupMission(
                context,
                List.of("C1", "S1", "D1"),
                waterTarget,
                ObjectifMission.SURVEILLANCE);

        assertEquals(StatutMission.EN_COURS, mission.getStatut());
        assertEquals(ObjectifMission.SURVEILLANCE, mission.getObjectif());
        assertEquals(2, mission.getActifsAssignes().size());

        List<String> assignedIds = mission.getActifsAssignes().stream().map(ActifMobile::getId).toList();
        assertTrue(assignedIds.contains("S1"));
        assertTrue(assignedIds.contains("D1"));
        assertFalse(assignedIds.contains("C1"));
    }
}
