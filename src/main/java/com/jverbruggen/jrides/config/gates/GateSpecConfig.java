package com.jverbruggen.jrides.config.gates;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.gate.GateType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class GateSpecConfig extends BaseConfig {
    private final List<GateConfig> gates;

    public GateSpecConfig(List<GateConfig> gates) {
        this.gates = gates;
    }

    public List<GateConfig> getGates() {
        return gates;
    }

    public static GateSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        List<List<Object>> positions = (List<List<Object>>) configurationSection.getList("positions");
        List<GateConfig> gates = new ArrayList<>();

        if(positions != null){
            for(List<Object> position : positions){
                if(position.size() == 3){
                    Vector3 location = new Vector3(toDouble(position.get(0)), toDouble(position.get(1)), toDouble(position.get(2)));
                    gates.add(new GateConfig(location, GateType.DOOR_OR_GATE));
                }else if(position.size() == 4){
                    Vector3 location = new Vector3(toDouble(position.get(0)), toDouble(position.get(1)), toDouble(position.get(2)));
                    GateType gateType = GateType.fromValue((String) position.get(3));
                    gates.add(new GateConfig(location, gateType));
                }else{
                    throw new RuntimeException("Gate position should be of format [x,y,z] or [x,y,z,'type']");
                }
            }
        }

        return new GateSpecConfig(gates);
    }
}
