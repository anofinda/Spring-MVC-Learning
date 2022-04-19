package com.example.MBean;

import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * @author dongyudeng
 */
@Component
@ManagedResource(objectName = "sample:name=userNumber", description = "Count user number")
public class UserNumberMBean {
    @Autowired
    UserService userService;

    @ManagedAttribute(description = "User number")
    public long getUserNumber(){
        return userService.getUserNumber();
    }
}
