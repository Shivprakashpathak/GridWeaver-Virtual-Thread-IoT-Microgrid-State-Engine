package com.gridweaver.config;

import com.gridweaver.model.DeviceEvent;
import com.gridweaver.model.DeviceState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<DeviceState, DeviceEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<DeviceState, DeviceEvent> states) throws Exception {
        states
            .withStates()
            .initial(DeviceState.IDLE)
            .states(EnumSet.allOf(DeviceState.class))
            .end(DeviceState.FAULT);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<DeviceState, DeviceEvent> transitions) throws Exception {
        transitions
            .withExternal()
            .source(DeviceState.IDLE).target(DeviceState.CHARGING).event(DeviceEvent.START_CHARGING)
            .and()
            .withExternal()
            .source(DeviceState.CHARGING).target(DeviceState.IDLE).event(DeviceEvent.STOP_CHARGING)
            .and()
            .withExternal()
            .source(DeviceState.IDLE).target(DeviceState.DISCHARGING).event(DeviceEvent.START_DISCHARGING)
            .and()
            .withExternal()
            .source(DeviceState.DISCHARGING).target(DeviceState.IDLE).event(DeviceEvent.STOP_DISCHARGING)
            .and()
            .withExternal()
            .source(DeviceState.CHARGING).target(DeviceState.FAULT).event(DeviceEvent.FAULT_DETECTED)
            .and()
            .withExternal()
            .source(DeviceState.DISCHARGING).target(DeviceState.FAULT).event(DeviceEvent.FAULT_DETECTED)
            .and()
            .withExternal()
            .source(DeviceState.IDLE).target(DeviceState.FAULT).event(DeviceEvent.FAULT_DETECTED)
            .and()
            .withExternal()
            .source(DeviceState.FAULT).target(DeviceState.IDLE).event(DeviceEvent.FAULT_CLEARED)
            .and()
            .withExternal()
            .source(DeviceState.IDLE).target(DeviceState.DISCHARGING).event(DeviceEvent.GRID_LOAD_HIGH)
            .and()
            .withExternal()
            .source(DeviceState.DISCHARGING).target(DeviceState.IDLE).event(DeviceEvent.GRID_LOAD_NORMAL);
    }
}
