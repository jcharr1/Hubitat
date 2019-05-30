/**
 *
 *  "Generic Z-Wave Contact/Switch Driver"
 *
 *	Although originally I ported this from ST it now has the 'switch' capability added
 *  I've also added the ability to switch on/off when open or closed - You choose
 * 
 *  Copyright 2018 Cobra
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Z-Wave Door/Window Sensor
 *
 *  Updated 30/05/2019
 *
 *  V1.3.1 - Debug
 *  V1.3.0 - Added ability to reverse open/closed operation independently from switch
 *  V1.2.0 - Added 'force Open' and 'Force Closed'
 *  V1.1.0 - Set initial state to off and cleaned up code a little
 *  V1.0.0 - POC
 */

metadata {
    definition(name: "Generic Z-Wave Contact/Switch Driver", namespace: "Cobra", author: "Cobra") { 
		capability "Contact Sensor"
        capability "Water Sensor"
		capability "Sensor"
		capability "Battery"
        capability "Switch"
        command "forceOpen"
        command "forceClosed"
        command "forceWet"
        command "forceDry"
        
		attribute "alarm", "string"
		attribute "DriverAuthor", "string"
        attribute "DriverVersion", "string"
        attribute "DriverStatus", "string"
		attribute "DriverUpdate", "string"
		attribute "lastCheckin", "string"
		attribute "checkInterval", "string"
        
//		fingerprint deviceId: " inClusters:0x30,0x80,0x84,0x71,0x70,0x85,0x86,0x72
		fingerprint deviceId: "0x2001", inClusters: "0x30,0x80,0x84,0x85,0x86,0x72"
		fingerprint deviceId: "0x07", inClusters: "0x30"
		fingerprint deviceId: "0x0701", inClusters: "0x5E,0x98"
		fingerprint deviceId: "0x0701", inClusters: "0x5E,0x86,0x72,0x98", outClusters: "0x5A,0x82"
		fingerprint deviceId: "0x0701", inClusters: "0x5E,0x80,0x71,0x85,0x70,0x72,0x86,0x30,0x31,0x84,0x59,0x73,0x5A,0x8F,0x98,0x7A", outClusters: "0x20"
		
		fingerprint mfr: "0086", prod: "0002", model: "001D", deviceJoinName: "Aeotec Door/Window Sensor (Gen 5)"
		fingerprint mfr: "0086", prod: "0102", model: "0070", deviceJoinName: "Aeotec Door/Window Sensor 6"
		fingerprint mfr: "0086", prod: "0102", model: "0059", deviceJoinName: "Aeotec Recessed Door Sensor"
		fingerprint mfr: "014A", prod: "0001", model: "0002", deviceJoinName: "Ecolink Door/Window Sensor"
		fingerprint mfr: "014A", prod: "0001", model: "0003", deviceJoinName: "Ecolink Tilt Sensor"
		fingerprint mfr: "011A", prod: "0601", model: "0903", deviceJoinName: "Enerwave Magnetic Door/Window Sensor"
		fingerprint mfr: "014F", prod: "2001", model: "0102", deviceJoinName: "Nortek GoControl Door/Window Sensor"
		fingerprint mfr: "0063", prod: "4953", model: "3031", deviceJoinName: "Jasco Hinge Pin Door Sensor"
		fingerprint mfr: "019A", prod: "0003", model: "0003", deviceJoinName: "Sensative Strips"
		fingerprint mfr: "0258", prod: "0003", model: "0082", deviceJoinName: "NEO Coolcam Door/Window Sensor"
		fingerprint mfr: "021F", prod: "0003", model: "0101", deviceJoinName: "Dome Door/Window Sensor"
		
        
        
        preferences(){
            
            section("Switch Mode"){

                input "mode", "bool", title: "Reverse Switch Mode", required: true, defaultValue: false   
                input "mode1", "bool", title:"Reverse Contact Mode", required: true, defaultValue: false
                input "mode2", "bool", title: "Reverse Water Mode", required: true, defaultValue: false
            }
            
        }
        
        
	}
}




private getCommandClassVersions() {
	[0x20: 1, 0x25: 1, 0x30: 1, 0x31: 5, 0x80: 1, 0x84: 1, 0x71: 3, 0x9C: 1]
}

def parse(String description) {
	def result = null
	if (description.startsWith("Err 106")) {
		if ((zwaveInfo.zw == null && state.sec != 0) || zwaveInfo?.zw?.endsWith("s")) {
			log.debug description
		} else {
			result = createEvent(
				descriptionText: "This sensor failed to complete the network security key exchange. If you are unable to control it via SmartThings, you must remove it from your network and add it again.",
				eventType: "ALERT",
				name: "secureInclusion",
				value: "failed",
				isStateChange: true,
			)
		}
	} else if (description != "updated") {
		def cmd = zwave.parse(description, commandClassVersions)
		if (cmd) {
			result = zwaveEvent(cmd)
		}
	}
//	log.debug "parsed '$description' to $result"
	return result
}

def installed() {
	version()
	sendEvent(name: "checkInterval", value: 2 * 4 * 60 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
	sendEvent(name: "battery", unit: "%", value: 100)
    off()
	response(initialPoll())
}

def updated() {
	version()
	sendEvent(name: "checkInterval", value: 2 * 4 * 60 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
    off()
}


def forceOpen(){
   sensorValueEvent(1) 
    
}
def forceClosed(){
    sensorValueEvent(0)  
}

def forceWet(){
   sensorValueEvent(1) 
    
}
def forceDry(){
    sensorValueEvent(0)  
}

def on(){
sensorValueEvent(1) 
   
}

def off(){
sensorValueEvent(0) 
	
}


def sensorValueEvent(value) {
    
// lastCheckin can be used with webCoRE
	sendEvent(name: "lastCheckin", value: now())
    
	if (value) {
		createEvent(name: "contact", value: "open", descriptionText: "$device.displayName is open")
        if(mode == false){ sendEvent(name: "switch", value: "on")}        
        if(mode1 == false){sendEvent(name: "contact", value: "open")}
        if(mode2 == false){sendEvent(name: "water", value: "wet")}
        if(mode == true){sendEvent(name: "switch", value: "off")}
        if(mode1 == true){sendEvent(name: "contact", value: "closed")}
       	if(mode2 == true){sendEvent(name: "water", value: "dry")}
         
	}
	else{
		createEvent(name: "contact", value: "closed", descriptionText: "$device.displayName is closed")
        if(mode == false){sendEvent(name: "switch", value: "off")}
        if(mode1 == false){sendEvent(name: "contact", value: "closed")}
        if(mode2 == false){sendEvent(name: "water", value: "dry")}
        if(mode == true){sendEvent(name: "switch", value: "on")}
        if(mode1 == true){sendEvent(name: "contact", value: "open")}
        if(mode2 == true){ sendEvent(name: "water", value: "wet")}
        
    }	
}

def zwaveEvent(hubitat.zwave.commands.basicv1.BasicReport cmd) {
	sensorValueEvent(cmd.value)
}

def zwaveEvent(hubitat.zwave.commands.basicv1.BasicSet cmd) {
	sensorValueEvent(cmd.value)
}

def zwaveEvent(hubitat.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
	sensorValueEvent(cmd.value)
}

def zwaveEvent(hubitat.zwave.commands.sensorbinaryv1.SensorBinaryReport cmd) {
	sensorValueEvent(cmd.sensorValue)
}

def zwaveEvent(hubitat.zwave.commands.sensoralarmv1.SensorAlarmReport cmd) {
	sensorValueEvent(cmd.sensorState)
}

def zwaveEvent(hubitat.zwave.commands.notificationv3.NotificationReport cmd) {
	def result = []
	if (cmd.notificationType == 0x06 && cmd.event == 0x16) {
		result << sensorValueEvent(1)
	} else if (cmd.notificationType == 0x06 && cmd.event == 0x17) {
		result << sensorValueEvent(0)
	} else if (cmd.notificationType == 0x07) {
		if (cmd.v1AlarmType == 0x07) {  // special case for nonstandard messages from Monoprice door/window sensors
			result << sensorValueEvent(cmd.v1AlarmLevel)
		} else if (cmd.event == 0x01 || cmd.event == 0x02) {
			result << sensorValueEvent(1)
		} else if (cmd.event == 0x03) {
			result << createEvent(descriptionText: "$device.displayName covering was removed", isStateChange: true)
			if (!state.MSR) result << response(command(zwave.manufacturerSpecificV2.manufacturerSpecificGet()))
		} else if (cmd.event == 0x05 || cmd.event == 0x06) {
			result << createEvent(descriptionText: "$device.displayName detected glass breakage", isStateChange: true)
		} else if (cmd.event == 0x07) {
			if (!state.MSR) result << response(command(zwave.manufacturerSpecificV2.manufacturerSpecificGet()))
			result << createEvent(name: "motion", value: "active", descriptionText: "$device.displayName detected motion")
		}
	} else if (cmd.notificationType) {
		def text = "Notification $cmd.notificationType: event ${([cmd.event] + cmd.eventParameter).join(", ")}"
		result << createEvent(name: "notification$cmd.notificationType", value: "$cmd.event", descriptionText: text, displayed: false)
	} else {
		def value = cmd.v1AlarmLevel == 255 ? "active" : cmd.v1AlarmLevel ?: "inactive"
		result << createEvent(name: "alarm $cmd.v1AlarmType", value: value, displayed: false)
	}
	result
}

def zwaveEvent(hubitat.zwave.commands.wakeupv1.WakeUpNotification cmd) {
	def event = createEvent(descriptionText: "${device.displayName} woke up", isStateChange: false)
	def cmds = []
	if (!state.MSR) {
		cmds << zwave.manufacturerSpecificV2.manufacturerSpecificGet()
	}

	if (device.currentValue("contact") == null) {
		// In case our initial request didn't make it, initial state check no. 3
		cmds << zwave.sensorBinaryV2.sensorBinaryGet(sensorType: zwave.sensorBinaryV2.SENSOR_TYPE_DOOR_WINDOW)
	}

	if (!state.lastbat || now() - state.lastbat > 53 * 60 * 60 * 1000) {
		cmds << zwave.batteryV1.batteryGet()
	}

	def request = []
	if (cmds.size() > 0) {
		request = commands(cmds, 1000)
		request << "delay 20000"
	}
	request << zwave.wakeUpV1.wakeUpNoMoreInformation().format()

	[event, response(request)]
}

def zwaveEvent(hubitat.zwave.commands.batteryv1.BatteryReport cmd) {
	def map = [name: "battery", unit: "%"]
	if (cmd.batteryLevel == 0xFF) {
		map.value = 1
		map.descriptionText = "${device.displayName} has a low battery"
		map.isStateChange = true
	} else {
		map.value = cmd.batteryLevel
	}
	state.lastbat = now()
	[createEvent(map)]
}

def zwaveEvent(hubitat.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
	def result = []

	def msr = String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId)
	log.debug "msr: $msr"
	updateDataValue("MSR", msr)

	result << createEvent(descriptionText: "$device.displayName MSR: $msr", isStateChange: false)

	// change Driver if required based on MSR
	if (!retypeBasedOnMSR()) {
		if (msr == "011A-0601-0901") {
			// Enerwave motion doesn't always get the associationSet that the hub sends on join
			result << response(zwave.associationV1.associationSet(groupingIdentifier: 1, nodeId: zwaveHubNodeId))
		}
	} else {
		// if this is door/window sensor check initial contact state no.2
		if (!device.currentState("contact")) {
			result << response(command(zwave.sensorBinaryV2.sensorBinaryGet(sensorType: zwave.sensorBinaryV2.SENSOR_TYPE_DOOR_WINDOW)))
		}
	}

	// every battery device can miss initial battery check. check initial battery state no.2
	if (!device.currentState("battery")) {
		result << response(command(zwave.batteryV1.batteryGet()))
	}

	result
}

def zwaveEvent(hubitat.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) {
	def encapsulatedCommand = cmd.encapsulatedCommand(commandClassVersions)
	if (encapsulatedCommand) {
		zwaveEvent(encapsulatedCommand)
	}
}

def zwaveEvent(hubitat.zwave.commands.crc16encapv1.Crc16Encap cmd) {
	def version = commandClassVersions[cmd.commandClass as Integer]
	def ccObj = version ? zwave.commandClass(cmd.commandClass, version) : zwave.commandClass(cmd.commandClass)
	def encapsulatedCommand = ccObj?.command(cmd.command)?.parse(cmd.data)
	if (encapsulatedCommand) {
		return zwaveEvent(encapsulatedCommand)
	}
}

def zwaveEvent(hubitat.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd) {
	def result = null
	def encapsulatedCommand = cmd.encapsulatedCommand(commandClassVersions)
	log.debug "Command from endpoint ${cmd.sourceEndPoint}: ${encapsulatedCommand}"
	if (encapsulatedCommand) {
		result = zwaveEvent(encapsulatedCommand)
	}
	result
}

def zwaveEvent(hubitat.zwave.commands.multicmdv1.MultiCmdEncap cmd) {
	log.debug "MultiCmd with $numberOfCommands inner commands"
	cmd.encapsulatedCommands(commandClassVersions).collect { encapsulatedCommand ->
		zwaveEvent(encapsulatedCommand)
	}.flatten()
}

def zwaveEvent(hubitat.zwave.Command cmd) {
	createEvent(descriptionText: "$device.displayName: $cmd", displayed: false)
}

def initialPoll() {
	def request = []
	if (isEnerwave()) { // Enerwave motion doesn't always get the associationSet that the hub sends on join
		request << zwave.associationV1.associationSet(groupingIdentifier: 1, nodeId: zwaveHubNodeId)
	}

	// check initial battery and contact state no.1
	request << zwave.batteryV1.batteryGet()
	request << zwave.sensorBinaryV2.sensorBinaryGet(sensorType: zwave.sensorBinaryV2.SENSOR_TYPE_DOOR_WINDOW)
	request << zwave.manufacturerSpecificV2.manufacturerSpecificGet()
	commands(request, 500) + ["delay 6000", command(zwave.wakeUpV1.wakeUpNoMoreInformation())]
}

private command(hubitat.zwave.Command cmd) {
//	if ((zwaveInfo.zw == null && state.sec != 0) || zwaveInfo?.zw?.endsWith("s")) {
//		zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
//	} else {
		cmd.format()
//	}
}

private commands(commands, delay = 200) {
	delayBetween(commands.collect { command(it) }, delay)
}

def retypeBasedOnMSR() {
	def dthChanged = true
	switch (state.MSR) {
		case "0086-0002-002D":
			log.debug "Changing device type to Z-Wave Water Sensor"
			setDeviceType("Z-Wave Water Sensor")
			break
		case "011F-0001-0001":  // Schlage motion
		case "014A-0001-0001":  // Ecolink motion
		case "014A-0004-0001":  // Ecolink motion +
		case "0060-0001-0002":  // Everspring SP814
		case "0060-0001-0003":  // Everspring HSP02
		case "011A-0601-0901":  // Enerwave ZWN-BPC
			log.debug "Changing device type to Z-Wave Motion Sensor"
			setDeviceType("Z-Wave Motion Sensor")
			break
		case "013C-0002-000D":  // Philio multi +
			log.debug "Changing device type to 3-in-1 Multisensor Plus (SG)"
			setDeviceType("3-in-1 Multisensor Plus (SG)")
			break
		case "0109-2001-0106":  // Vision door/window
			log.debug "Changing device type to Z-Wave Plus Door/Window Sensor"
			setDeviceType("Z-Wave Plus Door/Window Sensor")
			break
		case "0109-2002-0205": // Vision Motion
			log.debug "Changing device type to Z-Wave Plus Motion/Temp Sensor"
			setDeviceType("Z-Wave Plus Motion/Temp Sensor")
			break
		default:
			dthChanged = false
			break
	}
	dthChanged
}


private isEnerwave() {
	zwaveInfo?.mfr?.equals("011A") && zwaveInfo?.prod?.equals("0601") && zwaveInfo?.model?.equals("0901")
}






def version(){
    unschedule()
    schedule("0 0 8 ? * FRI *", updateCheck)  
    updateCheck()
}

def updateCheck(){

    setVersion()
	def paramsUD = [uri: "http://update.hubitat.uk/cobra.json"]
       	try {
        httpGet(paramsUD) { respUD ->
 //  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def newVerRaw = (respUD.data.versions.Driver.(state.InternalName))
            def newVer = (respUD.data.versions.Driver.(state.InternalName).replace(".", ""))
       		def currentVer = state.Version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Driver.(state.InternalName))
                state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.Status = "<b>** This driver is no longer supported by $state.author  **</b>"       
            log.warn "** This driver is no longer supported by $state.author **"      
      		}           
		else if(currentVer < newVer){
        	state.Status = "<b>New Version Available (Version: $newVerRaw)</b>"
        	log.warn "** There is a newer version of this driver available  (Version: $newVerRaw) **"
        	log.warn "** $state.UpdateInfo **"
       		} 
		else{ 
      		state.Status = "Current"
      		log.info "You are using the current version of this driver"
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
   		if(state.Status == "Current"){
			state.UpdateInfo = "N/A"
		    sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
	 	    sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
			}
    	else{
	    	sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
	     	sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
	    }   
 			sendEvent(name: "DriverAuthor", value: state.author, isStateChange: true)
    		sendEvent(name: "DriverVersion", value: state.Version, isStateChange: true)
    
     if(state.status){
         state.status = ""
         log.warn " Removing previous variables"
        state.remove(state.status)
     }
    		
}

def setVersion(){
		state.Version = "1.3.1"	 
		state.InternalName = "ContactSwitch"
}

