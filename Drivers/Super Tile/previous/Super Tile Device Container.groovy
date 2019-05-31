/**
 *  SuperTile Virtual Container
 *
 *  Copyright 2018 Andrew Parker
 * 
 * This container was created using code from Stephan Hackett and I thank him for his contribution to this project
 *
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
 *  
 *  02/04/2019 - edited to strip it down to Super Tile devices only
 *
 *
 *
 */

def version() {"1.0"}

metadata {
	definition (name: "Super Tile Device Container", namespace: "Cobra", author: "Andrew Parker") {
  //      capability "Refresh"
        attribute "NumberOfDevices", "number"	//stores the total number of child switches created by the container
        command "createNewVirtualDevice", ["NAME"] // , "TYPE"] //creates a new Virtual Device of the type specified and with entered label
    }
}

preferences {
preferences {
input "typeOfDevice", "enum", required: true, title: "Select Type ", submitOnChange: true,  defaultValue:  "Standard Tile Display Device", options: deviceList1()
}
}


def deviceList1(){
deviceInput = [
       "Standard Tile Display Device",
 //      "Countdown Tile Display Device",
 //      "Icon Tile Display Device",
//		"Alert Tile Display Device",
	"Other Devices Coming Soon..."
       
]  
    
    return deviceInput
}
   


def installed() {
	log.debug "Installing and configuring Virtual Container"
    state.vsIndex = 0 //stores an index value so that each newly created Virtual Switch has a unique name (simply incremements as each new device is added and attached as a suffix to DNI)
    initialize()
}

def initialize() {
	log.debug "Initializing Virtual Container"
    state.device1 = typeOfDevice
    state.version = version()
    updateSize()
}

def updated() {initialize()}

def refresh() {
	log.debug "Refreshing Container values"
    updateLabels()
    updateSize()
    if(!state.vsIndex) state.vsIndex = 0
}


def updateSize() {
	int mySize = getChildDevices().size()
    sendEvent(name:"NumberOfDevices", value: mySize)
}

def createNewVirtualDevice(vName){
   state.vsIndex = state.vsIndex + 1	//increment even on invalid device type
    switch(state.device1){
        case ["Standard Tile Display Device"]:
        	log.info "Creating Virtual ${state.device1} Device: ${vName}"
			childDevice = addChildDevice("Cobra", "SuperTileDisplayDevice", "VS-${device.deviceNetworkId}-${state.vsIndex}", [label: "${vName}", isComponent: false])
    		updateSize()
        break
        case ["Countdown Tile Display Device"]:
        	log.info "Creating Virtual  Device: ${vName}"
			childDevice = addChildDevice("Cobra", "SuperTileTimeDisplay", "VS-${device.deviceNetworkId}-${state.vsIndex}", [label: "${vName}", isComponent: false])
    		updateSize()
        break
		 case ["Icon Tile Display Device"]:
        	log.info "Creating Virtual  Device: ${vName}"
			childDevice = addChildDevice("Cobra", "SuperTileIconDisplay", "VS-${device.deviceNetworkId}-${state.vsIndex}", [label: "${vName}", isComponent: false])
    		updateSize()
        break
		case ["Alert Tile Display Device"]:
        	log.info "Creating Virtual  Device: ${vName}"
			childDevice = addChildDevice("Cobra", "SuperTileAlertDisplay", "VS-${device.deviceNetworkId}-${state.vsIndex}", [label: "${vName}", isComponent: false])
    		updateSize()
        break
}
	
}
	
	

def updateLabels() { // syncs device label with componentLabel data value
    def myChildren = getChildDevices()
    myChildren.each{
        log.debug it.data.label
        if(it.label != it.data.label) {
            it.updateDataValue("label", it.label)
        }
    }
}