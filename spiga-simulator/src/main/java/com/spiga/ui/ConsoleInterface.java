package com.spiga.ui;

import com.spiga.app.SimulationBootstrap;
import com.spiga.app.SimulationContext;
import com.spiga.core.ActifMobile;
import com.spiga.core.EtatOperationnel;
import com.spiga.core.Point3D;
import com.spiga.mission.Mission;
import com.spiga.mission.MissionReconnaissance;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console-based interface for the simulation.
 * Provides a text-based menu to interact with the fleet manager.
 */
public class ConsoleInterface {
    private final SimulationContext context;
    private final SimulationBootstrap bootstrap;
    private final Scanner scanner;

    public ConsoleInterface(SimulationContext context, SimulationBootstrap bootstrap) {
        this.context = context;
        this.bootstrap = bootstrap;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the console interface loop.
     * Displays a menu and processes user input.
     */
    public void demarrer() {
        boolean running = true;
        while (running) {
            try {
                System.out.println("\n--- SPIGA Simulator Console ---");
                System.out.println("1. Ajouter un actif");
                System.out.println("2. Lister la flotte");
                System.out.println("3. Créer une mission");
                System.out.println("4. Simuler un pas de temps");
                System.out.println("5. Quitter");
                System.out.print("Choix: ");

                String choix = scanner.nextLine();
                switch (choix) {
                    case "1":
                        ajouterActif();
                        break;
                    case "2":
                        listerFlotte();
                        break;
                    case "3":
                        creerMission();
                        break;
                    case "4":
                        simulerPasDeTemps();
                        break;
                    case "5":
                        running = false;
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du traitement de la commande: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void ajouterActif() {
        try {
            System.out.print("ID: ");
            String id = scanner.nextLine();

            if (id == null || id.trim().isEmpty()) {
                System.out.println("Erreur: L'ID ne peut pas être vide.");
                return;
            }

            System.out.println("Type (1: Recon, 2: Logistique, 3: Surface, 4: Sous-marin, 5: Terrestre): ");
            String type = scanner.nextLine();

            ActifMobile actif = bootstrap.createAssetFromConsoleChoice(type, id);
            Optional<String> validationError = bootstrap.validateSpawn(
                    actif.getClass().getSimpleName(),
                    actif.getPosition(),
                    context.getZone());

            if (validationError.isPresent()) {
                System.out.println(validationError.get());
                return;
            }

            bootstrap.registerAsset(context, actif);
            System.out.println("Actif ajouté : " + actif.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de l'actif: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listerFlotte() {
        try {
            System.out.println("\n--- Flotte ---");
            if (context.getGestionnaire().getFlotte().isEmpty()) {
                System.out.println("Aucun actif dans la flotte.");
                return;
            }

            for (ActifMobile actif : context.getGestionnaire().getFlotte()) {
                System.out.printf("%s [%s] - %s - Pos: %s%n",
                        actif.getId(),
                        actif.getClass().getSimpleName(),
                        actif.getEtat(),
                        actif.getPosition());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'affichage de la flotte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void creerMission() {
        try {
            System.out.print("ID Mission: ");
            String id = scanner.nextLine();

            if (id == null || id.trim().isEmpty()) {
                System.out.println("Erreur: L'ID de mission ne peut pas être vide.");
                return;
            }

            Mission mission = new MissionReconnaissance(id);

            ActifMobile asset = context.getGestionnaire().suggererActif(10);
            if (asset != null) {
                mission.assignerActif(asset);
                mission.demarrer();
                System.out.println("Mission " + id + " assignée à " + asset.getId() + " et démarrée.");
            } else {
                System.out.println("Aucun actif disponible pour cette mission.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de la mission: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void simulerPasDeTemps() {
        try {
            System.out.println("Simulation d'un pas de temps...");
            Point3D target = new Point3D(100, 100, 50);

            if (context.getGestionnaire().getFlotte().isEmpty()) {
                System.out.println("Aucun actif à déplacer.");
                return;
            }

            for (ActifMobile actif : context.getGestionnaire().getFlotte()) {
                try {
                    if (actif.getEtat() == EtatOperationnel.EN_MISSION || actif.getEtat() == EtatOperationnel.AU_SOL) {
                        actif.deplacer(target, context.getZone());
                        System.out.println(actif.getId() + " déplacé vers " + actif.getPosition());
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors du déplacement de " + actif.getId() + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SimulationBootstrap bootstrap = new SimulationBootstrap();
        SimulationContext context = bootstrap.createEmptyContext();
        ConsoleInterface console = new ConsoleInterface(context, bootstrap);
        console.demarrer();
    }
}
