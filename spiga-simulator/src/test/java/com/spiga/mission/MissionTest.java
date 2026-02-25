package com.spiga.mission;

import com.spiga.core.ActifMobile;
import com.spiga.core.DroneReconnaissance;
import com.spiga.core.EtatOperationnel;
import com.spiga.core.Point3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MissionTest {

    private Mission mission;
    private ActifMobile drone;

    @BeforeEach
    public void setUp() {
        mission = new MissionReconnaissance("M1");
        drone = new DroneReconnaissance("D1", new Point3D(0, 0, 0));
    }

    @Test
    public void testAssignAssetAvoidsDuplicates() {
        mission.assignerActif(drone);
        mission.assignerActif(drone);

        assertEquals(1, mission.getActifsAssignes().size());
        assertEquals("D1", mission.getActifsAssignes().get(0).getId());
    }

    @Test
    public void testStartMission() {
        mission.assignerActif(drone);
        mission.demarrer();

        assertEquals(StatutMission.EN_COURS, mission.getStatut());
        assertEquals(EtatOperationnel.EN_MISSION, drone.getEtat());
    }

    @Test
    public void testTerminateMission() {
        mission.assignerActif(drone);
        mission.demarrer();
        mission.terminer();

        assertEquals(StatutMission.TERMINEE, mission.getStatut());
        assertEquals(EtatOperationnel.AU_SOL, drone.getEtat());
    }

    @Test
    public void testMissionType() {
        assertEquals(ObjectifMission.RECONNAISSANCE, mission.getObjectif());

        mission.setObjectif(ObjectifMission.SURVEILLANCE);
        assertEquals(ObjectifMission.SURVEILLANCE, mission.getObjectif());
    }
}
