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
 *  Last Update: 18/04/2019
 *  Changes:
 *
 * 
 * added character numbers for each attribute
 * added countdown to date/time
 *  
 *  V1.0.0 - POC
 *
 */


metadata {	
definition (name: "SuperTileCountdownDisplay", namespace: "Cobra", author: "AJ Parker", importUrl: "https://raw.githubusercontent.com/CobraVmax/Hubitat/master/Drivers/Super%20Tile/Super%20Tile%20Time%20Device.groovy"){
	capability "Sensor"
	command "startTimer"
	command "stopTimer"
	command "refresh"
	command "setCountDownSeconds", ["number"]
	command "daysLeft", ["string"]
	command "hoursLeft",["string"]
	command "minutesLeft", ["string"]
	command "timeLeft", ["string"]
	
	
attribute "DaysLeft", "string"
attribute "HoursLeft", "string"
attribute "MinutesLeft", "string"	
attribute "TimeLeft", "string"	
attribute "Countdown", "string"
attribute "CountStatus", "string"
attribute "DriverVersion", "string"
attribute "DriverStatus", "string"
attribute "DriverUpdate", "string"
attribute " ", "string"
attribute "CountCharNumber - Countdown", "string"
attribute "CountCharNumber - DaysLeft", "string"	
attribute "CountCharNumber - MinutesLeft", "string"
attribute "CountCharNumber - HoursLeft", "string"	
attribute "CountCharNumber - TimeLeft", "string"	

}	

	preferences() {
	input "fweight", "enum",  title: "Font Weight", submitOnChange: true, defaultValue: "Normal", options: ["Normal", "Bold"]
	input "fstyle", "enum",  title: "Font Style", submitOnChange: true, defaultValue: "Normal", options: ["Normal", "Italic"]
	input "fcolour", "text",  title: "Font Colour (Hex Value)", defaultValue:"FFFFFF", submitOnChange: true
	input "fsize", "number",  title: "Initial Font Size", defaultValue:"25", submitOnChange: true
	input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false }}   

def initialize(){updated()}
def updated() {
version()
log.info "Updated called"	
logCheck()
setFont()
}


def setCountDownSeconds(secsIn){state.count = secsIn}
def startTimer(){
state.countNow = state.count
if(clockMode == true){stopClock()}
runIn(1, reduce)
send()}

def stopTimer(){
	state.countNow = 0}

def send(){
state.finalSend =""
state.finalSend += "<div style='color: #$state.fc;font-size:$state.fs"
state.finalSend += "px;font-weight: $state.fw; $state.stl'>"
state.finalSend +="${state.countNow}"
state.finalSend += "</div>"
state.CountCharNumber = state.finalSend.length()
sendEvent(name: "CountCharNumber - Countdown", value: state.CountCharNumber)	
sendEvent(name: "Countdown", value: state.finalSend, isStateChange: true)	
sendEvent(name: "CountStatus", value: state.status)}


def daysLeft(evt){
state.DaysLeft = evt
state.finalSend1 =""
state.finalSend1 += "<div style='color: #$state.fc;font-size:$state.fs"
state.finalSend1 += "px;font-weight: $state.fw; $state.stl'>"
state.finalSend1 +="${state.DaysLeft}"
state.finalSend1 += "</div>"
state.CountCharNumber1 = state.finalSend1.length()
sendEvent(name: "CountCharNumber - DaysLeft", value: state.CountCharNumber1)	
sendEvent(name: "DaysLeft", value: state.finalSend1, isStateChange: true)}	

def hoursLeft(evt){
state.HoursLeft = evt
state.finalSend2 =""
state.finalSend2 += "<div style='color: #$state.fc;font-size:$state.fs"
state.finalSend2 += "px;font-weight: $state.fw; $state.stl'>"
state.finalSend2 +="${state.HoursLeft}"
state.finalSend2 += "</div>"
state.CountCharNumber2 = state.finalSend2.length()
sendEvent(name: "CountCharNumber - HoursLeft", value: state.CountCharNumber2)	
sendEvent(name: "HoursLeft", value: state.finalSend2, isStateChange: true)}


def minutesLeft(evt){
state.MinutesLeft = evt
state.finalSend3 =""
state.finalSend3 += "<div style='color: #$state.fc;font-size:$state.fs"
state.finalSend3 += "px;font-weight: $state.fw; $state.stl'>"
state.finalSend3 +="${state.MinutesLeft}"
state.finalSend3 += "</div>"	
state.CountCharNumber3 = state.finalSend3.length()
sendEvent(name: "CountCharNumber - MinutesLeft", value: state.CountCharNumber3)	
sendEvent(name: "MinutesLeft", value:state.finalSend3, isStateChange: true)	}

def timeLeft(evt){
state.TimeLeft = evt
state.finalSend4 =""
state.finalSend4 += "<div style='color: #$state.fc;font-size:$state.fs"
state.finalSend4 += "px;font-weight: $state.fw; $state.stl'>"
state.finalSend4 +="${state.TimeLeft}"
state.finalSend4 += "</div>"
state.CountCharNumber4 = state.finalSend4.length()
sendEvent(name: "CountCharNumber - TimeLeft", value: state.CountCharNumber4)	
sendEvent(name: "TimeLeft", value:state.finalSend4, isStateChange: true)}


def refresh(){
	LOGDEBUG("Refresh Called...")
	sendEvent(name: "DaysLeft", value: state.finalSend1, isStateChange: true)
	sendEvent(name: "HoursLeft", value: state.finalSend2, isStateChange: true)
	sendEvent(name: "MinutesLeft", value:state.finalSend3, isStateChange: true)	
	sendEvent(name: "TimeLeft", value:state.finalSend4, isStateChange: true)
}


def reduce(){
if(state.countNow > 0){
state.status = "Active"		
state.countNow = state.countNow -1
send()	
runIn(1,reduce)}
if(state.countNow < 1 ){
state.status = "Stopped"	
if(clockMode == true){startClock()}
sendEvent(name: "CountStatus", value: state.status)
send()}}	
def setFont(){
dFontW()
dFontC()
dFontS()
fStyle()}
def dFontC(){state.fc = "${fcolour}"}
def dFontS(){state.fs = "${fsize}"}
def dFontW(){
if(fweight == "Normal"){state.fw = "normal"}
if(fweight == "Bold"){state.fw = "bold"	}
if(fweight == "Italic"){
state.stl = "font-style: italic"}}
def fStyle(){
if(fstyle == "Italic"){state.stl = "font-style: italic"}
if(fstyle != "Italic"){state.stl= "font-style: normal"}}
def logCheck(){
state.checkLog = debugMode
if(state.checkLog == true){log.info "All Logging Enabled"}
if(state.checkLog == false){log.info "Debug Logging Disabled"}
if(debugMode){runIn(1800, logsOff)}}

def logsOff() {
log.warn "Debug logging disabled..."
device.updateSetting("debugMode", [value: "false", type: "bool"])}


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
    state.version = "1.0.0"
    state.InternalName = "SuperTileCountDownDisplay"
   	state.CobraAppCheck = "supertilecountdowndriver.json"     
}










