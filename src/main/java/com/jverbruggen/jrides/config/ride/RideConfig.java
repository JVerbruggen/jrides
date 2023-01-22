package com.jverbruggen.jrides.config.ride;

import com.jverbruggen.jrides.animator.GCRideHandle;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.World;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RideConfig {
    private List<RideConfigObject> rides;
    private File dataFolder;
    private RideManager rideManager;

    public RideConfig(File dataFolder, RideManager rideManager) {
        this.rides = new ArrayList<RideConfigObject>();
        this.dataFolder = dataFolder;
        this.rideManager = rideManager;
    }

    public List<RideConfigObject> getRides() {
        return rides;
    }

    public void initAllRides(World world){
        List<String> rideIdentifiers = getLoadableRides();

        for (String rideIdentifier : rideIdentifiers) {
            loadFromConfig(world, rideIdentifier);
        }
    }

    public List<String> getLoadableRides(){
        return List.of("black_mamba");
    }

    public void loadFromConfig(World world, String rideIdentifier){
        String configFileName = "coasters/" + rideIdentifier + ".csv";
        File configFile = new File(dataFolder, configFileName);
        Path pathToConfigFile = configFile.toPath();
        List<NoLimitsExportPositionRecord> positions = new ArrayList<>();

        try(BufferedReader br = Files.newBufferedReader(pathToConfigFile, StandardCharsets.UTF_8)){
            String line = br.readLine(); // Skip header line
            line = br.readLine();
            while(line != null){
                String[] attributes = line.split("\t");
                NoLimitsExportPositionRecord record = NoLimitsExportPositionRecord.createFromCSVAttributes(attributes);
                positions.add(record);

                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Ride ride = null;
        GCRideHandle rideHandle = new GCRideHandle(ride, positions, world);
        rideManager.addRideHandle(rideHandle);
    }
}
