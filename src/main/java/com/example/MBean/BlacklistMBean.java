package com.example.MBean;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author dongyudeng
 */
@Component
@ManagedResource(objectName = "sample:name=blacklist",description = "Blacklist of IP addresses")
public class BlacklistMBean {
    private Set<String> ipSet = new HashSet<>();

    @ManagedAttribute(description = "get Blacklist")
    public String[] getBlacklist() {
        return ipSet.toArray(String[]::new);
    }

    @ManagedOperation
    @ManagedOperationParameter(name="ip",description = "Target IP to add")
    public void add(String ip) {
        ipSet.add(ip);
    }

    @ManagedOperation
    @ManagedOperationParameter(name="ip",description = "Target IP to remove")
    public void remove(String ip){
        ipSet.remove(ip);
    }

    public boolean checkIn(String ip){
        return ipSet.contains(ip);
    }
}
