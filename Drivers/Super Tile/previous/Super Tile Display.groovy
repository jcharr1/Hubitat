/**
 * Please Note: This driver is NOT released under any open-source license.
 * Please be sure to read the license agreement before installing this code.
 *
 * Copyright 2019 Andrew Parker, Parker IT North East Limited
 *
 * This driver is created and licensed by Parker IT North East Limited. A United Kingdom, Limited Company.
 *
 * This software, along with associated elements, including but not limited to online and/or electronic documentation are
 * protected by international laws and treaties governing intellectual property rights.
 *
 * This software has been licensed to you. All rights are reserved. You may use and/or modify the software.
 * You may not sublicense or distribute this software or any modifications to third parties in any way.
 *
 * You may not distribute any part of this software without the author's express permission
 *
 * By downloading, installing, and/or executing this software you hereby agree to the terms and conditions set forth in the Software license agreement.
 * This agreement can be found on-line at: http://hubitat.uk/software_License_Agreement.txt
 * 
 * Hubitat is the trademark and intellectual property of Hubitat Inc. 
 * Parker IT North East Limited has no formal or informal affiliations or relationships with Hubitat.
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License Agreement
 * for the specific language governing permissions and limitations under the License.
 *
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  
 *  Last Update: 06/04/2019
 *
 *  Changes:
 *
 *  V2.0.0 - Recode - Major functions now moved to app
 *  V1.1.1 - debug refresh
 *  V1.1.0 - Added initial state so it should immediately display attribute in tile selection on dashboard
 *  V1.0.0 - POC
 *
 */


metadata {
		definition (name: "SuperTileDisplayDevice", namespace: "Cobra", author: "AJ Parker", importUrl: "https://raw.githubusercontent.com/CobraVmax/Hubitat/master/Drivers/Super%20Tile/Super%20Tile%20Display.groovy"){
		capability "Sensor"
		command "clearData", ["string"]
		command "tileIn", ["string"]	
		command "setCurrentMode", ["string"]
		command "setHSMstate", ["string"]
		command "overRide", ["string"]
		command "refresh"
		attribute "OverRideStatus", "string"
		attribute "CustomDisplay", "string"
		attribute "CurrentMode", "string"
		attribute "CurrentHSMstatus", "string"
		attribute "CharacterNumber", "string"  
		attribute  " ", "string"
        attribute "DriverVersion", "string"
        attribute "DriverStatus", "string"
        attribute "DriverUpdate", "string"
		}
	preferences(){input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false}
}	
			
			
			
def initialize(){updated()}	
def updated() {
    log.info "Updated called"
    version()
	logCheck()
	initialState()}			
			
def clearData(){
	state.tileIn = " "
	state.modeNow = " "
	state.hsmNow = " "
	state.CharNumber = " "
	sendEvent(name: "CustomDisplay", value: state.tileIn, isStateChange: true)
	sendEvent(name: "CurrentMode", value: state.modeNow, isStateChange: true)
	sendEvent(name: "CurrentHSMstatus", value: state.hsmNow, isStateChange: true)
	sendEvent(name: "CharacterNumber", value: state.CharNumber, isStateChange: true)
	
}
def overRide(overRideIn){
	state.overRide = overRideIn
	if(state.overRide != "Stop"){
	LOGDEBUG("OVERRIDE = $state.overRide")
	sendEvent(name: "CustomDisplay", value: state.overRide)
	sendEvent(name: "OverRideStatus", value: "Running", isStateChange: true)
	}
	if(state.overRide == "Stop"){
		refresh()
		sendEvent(name: "OverRideStatus", value: "Stopped", isStateChange: true)
	LOGDEBUG("OVERRIDE = $state.overRide")
	}
}	

def setCurrentMode(modeIn){
	state.modeNow = modeIn
	sendEvent(name: "CurrentMode", value: state.modeNow)
}

def setHSMstate(hsmIn){
	state.hsmNow = hsmIn
	sendEvent(name: "CurrentHSMstatus", value: state.hsmNow)
}

def logsOff() {
    log.warn "Debug logging disabled..."
	device.updateSetting("debugMode", [value: "false", type: "bool"])
}

def initialState(){
	sendEvent(name: "CustomDisplay", value: "Awaiting Tile Data")
	sendEvent(name: "CurrentMode", value: "Awaiting Mode Data")
	sendEvent(name: "CurrentHSMstatus", value: "Awaiting HSM Data")		  
}

def tileIn(tileInData){
	state.tileIn = tileInData
	state.CharNumber = state.tileIn.length()
	if(state.CharNumber > 1024){
	LOGDEBUG("Too many characters for an attribute tile")
	overRide("Unable to Display<br>Please Check Character Number<br>(1024 Max)")
	}
	if(state.CharNumber < 1024){
		sendEvent(name: "CustomDisplay", value: state.tileIn)
		}
	LOGDEBUG( "Attribute Content: $state.tileIn")
	sendEvent(name: "CharacterNumber", value: state.CharNumber)
}
			
def refresh(){
	sendEvent(name: "CustomDisplay", value: state.tileIn, isStateChange: true)
	sendEvent(name: "CurrentMode", value: state.modeNow, isStateChange: true)
	sendEvent(name: "CurrentHSMstatus", value: state.hsmNow, isStateChange: true)
	sendEvent(name: "CharacterNumber", value: state.CharNumber, isStateChange: true)
}


def logCheck(){
	state.checkLog = debugMode
	if(state.checkLog == true){
	log.info "Debug Logging Enabled"
}
	else if(state.checkLog == false){
	log.info "Debug Logging Disabled"
}
if(debugMode){runIn(1800, logsOff)}
}
def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("Device Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}


def version(){
    unschedule()
    schedule("0 30 9 ? * FRI *", updateCheck)  
    updateCheck()
}

def updateCheck(){
    setVersion()
	def paramsUD = [uri: "http://update.hubitat.uk/json/${state.CobraAppCheck}"] 
       	try {
        httpGet(paramsUD) { respUD ->
//			log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code **********************
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def newVerRaw = (respUD.data.versions.Driver.(state.InternalName))
//			log.warn "$state.InternalName = $newVerRaw"
  			def newVer = newVerRaw.replace(".", "")
//			log.warn "$state.InternalName = $newVer"
			state.newUpdateDate = (respUD.data.Comment)
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = "Updated: "+state.newUpdateDate + " - "+(respUD.data.versions.UpdateInfo.Driver.(state.InternalName))
            state.author = (respUD.data.author)
			state.icon = (respUD.data.icon)
           
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
		    sendEvent(name: "DriverUpdate", value: state.UpdateInfo)
	 	    sendEvent(name: "DriverStatus", value: state.Status)
			}
    	else{
	    	sendEvent(name: "DriverUpdate", value: state.UpdateInfo)
	     	sendEvent(name: "DriverStatus", value: state.Status)
	    }   
 			sendEvent(name: " ", value: state.icon +"<br>" +state.Copyright +"<br> <br>")
    		sendEvent(name: "DriverVersion", value: state.version)
}

def setVersion(){
    state.version = "2.0.0"
    state.InternalName = "DisplayTile"
   	state.CobraAppCheck = "displaytile.json"     
}




			
			
			
			
			
			
			
			