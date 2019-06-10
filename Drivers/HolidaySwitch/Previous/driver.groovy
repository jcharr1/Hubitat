/**
 * Holiday Switch Driver
 *
 *  Copyright 2019 Andrew Parker
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
 *  Last Update 09/05/2019
 *
 *
 *  V1.0.0 - POC
 *
 */

metadata {
    definition (name: "HolidaySwitch", namespace: "Cobra", author: "Andrew Parker", importUrl: "https://raw.githubusercontent.com/CobraVmax/Hubitat/master/Drivers/HolidaySwitch/driver.groovy") {
        capability "Switch"
//      command "OverrideOn", ["string"] // ready for the app
//		command "OverrideOff" // ready for the app
        attribute  "override", "string"
		attribute  "holiday", "string"
    	attribute  " ", "string"
        attribute "DriverVersion", "string"
        attribute "DriverStatus", "string"
        attribute "DriverUpdate", "string" 
        
    }
    preferences() {
      
            input "apiKey", "text", required: true, title: 'API Key  <br>(Get a free calendarific.com API key <a href= "https://calendarific.com">Here</a>)'
            input "location1", "text", required: true, title: "Country"	
			input "type1", "enum", title: "Select Holiday Type", required: true, defaultValue: "National", options: [ "National", "Local", "Religious"]
		if(type1 == "Religious"){
			input "type2", "enum", title: "Select Holiday Type", required: true, defaultValue: "National", options: [ "Buddhism", "Christian", "Hinduism", "Muslim", "Hebrew"]
		}
        	input name: "logInfo", type: "bool", title: "Enable informational logging", defaultValue: false
        	input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: false
           
			
        
    }
}

def updated() {
//	state.clear()
	state.override = false
    log.info "updated..."
    log.info "debug logging is: ${logEnable == true}"
	log.info "info logging is: ${logInfo == true}"
	def random = new Random()
    Integer randomHour = random.nextInt(18-10) + 10
    Integer randomDayOfWeek = random.nextInt(7-1) + 1 // 1 to 7
    schedule("0 0 " + randomHour + " ? * " + randomDayOfWeek, updateCheck) 
	updateCheck()
    if (logEnable) runIn(1800, logsOff)
	schedule("0 1 0 1/1 * ? *", checkHolidays)
	checkHolidays()
	
}

def logsOff() {
    log.info "debug logging disabled..."
    device.updateSetting("logEnable", [value: "false", type: "bool"])
}

def on() {
	if(state.data1 == null){state.data1 = "Holiday Title Not Defined"}
	sendEvent(name: "switch", value: "on")
	sendEvent(name: "holiday", value: state.data1)
	if(logInfo){ log.info "Switch On"}

}

def off() {
	sendEvent(name: "switch", value: "off")
	sendEvent(name: "holiday", value: " ")
	if(logInfo){ log.info "Switch Off"}

}

def OverrideOn(evt){
	state.data1 = evt
	if(state.data1 == null){state.data1 = "Holiday Title Not Defined"}
	state.override = true
on()
}	
def OverrideOff(){
	state.override = false
	checkHolidays()
//	off()
}	

def checkHolidays(){
def dayNum = new Date().format('dd', location.timeZone)
def monthNum = new Date().format('MM', location.timeZone)
def yearNum = new Date().format('yyyy', location.timeZone)
if(logEnable) {log.debug "formatted: Day= $dayNum Month = $monthNum Year = $yearNum"}
if(state.override == false){

    def params1 = [ 
	uri:"https://calendarific.com/api/v2/holidays?&api_key=${apiKey}&country=${location1}&type=${type1}&year=${yearNum}&day=${dayNum}&month=${monthNum}"	
//	uri:"https://calendarific.com/api/v2/holidays?&api_key=${apiKey}&country=GB&type=${type1}&year=2019&day=06&month=05"	// Test UK National Holiday & Muslim Holiday (Ramadam Start)
//	uri:"https://calendarific.com/api/v2/holidays?&api_key=${apiKey}&country=GB&type=${type1}&year=2019&day=09&month=05"	// Test UK Hebrew Holiday (Yom HaAtzmaut)
//	uri:"https://calendarific.com/api/v2/holidays?&api_key=${apiKey}&country=GB&type=${type1}&year=2019&day=06&month=01"	// Test UK Christian Holiday (Epiphany)
		
		
		
    ]

    try {
        httpGet(params1) { resp1 ->
            resp1.headers.each {
				if(logEnable == true){log.debug "Response1: ${it.name} : ${it.value}"}
        }
            if(logEnable == true){  
           
            log.debug "params1: ${params1}"
            log.debug "response contentType: ${resp1.contentType}"
 		    log.debug "response data: ${resp1.data}"
            } 
			
		
			if(type1 == "Religious"){     
			state.data2 = resp1.data.response.holidays.type[0]
				if(state.data2 != null){
			if(state.data2.contains(type2)){
			state.data1 = resp1.data.response.holidays.name[0]
				if(logEnable) {log.debug "****   Religious Holiday found - $state.data2   ****"}
				on()
			}
				if(!state.data2.contains(type2)){state.data1 = " "
				off()}
			   }
			}
			if(type1 != "Religious"){
			state.data1 = resp1.data.response.holidays.name[0]           	
			if(state.data1){on()}
			if(!state.data1){
				off()
				if(logEnable){ log.debug "Not today"}}
			   }
			   
		}
	
 }	  catch (e) {
        log.error "something went wrong: $e"
    }
	}
	else{
		if(logInfo){ log.info "Override is on so not checking for national holidays"}}
}



def updateCheck(){
    setVersion()
	def paramsUD = [uri: "http://update.hubitat.uk/json/${state.CobraAppCheck}"] 
       	try {
        httpGet(paramsUD) { respUD ->
//  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code **********************
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def newVerRaw = (respUD.data.versions.Driver.(state.InternalName))
	//		log.warn "$state.InternalName = $newVerRaw"
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
		    sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
	 	    sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
			}
    	else{
	    	sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
	     	sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
	    }   
 			sendEvent(name: " ", value: state.icon +"<br>" +state.Copyright +"<br>", isStateChange: true)
    		sendEvent(name: "DriverVersion", value: state.version +"<br><br>", isStateChange: true)
    
    
    	//	
}

def setVersion(){
    state.version = "1.0.0"
    state.InternalName = "HolidaySwitchDriver"
   	state.CobraAppCheck = "holidayswitchdriver.json"
    sendEvent(name: "DriverAuthor", value: "Cobra", isStateChange: true)
    sendEvent(name: "DriverVersion", value: state.version, isStateChange: true)
    
    
      
}





