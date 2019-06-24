/**
 * Custom WU Driver
 *
 *  Copyright 2019 Andrew Parker
 *
 *  This driver was originally written by @mattw01 and I thank him for that!
 *  Heavily modified by myself: @Cobra with lots of help from @Scottma61 ( @Matthew )
 *  and with valuable input from the Hubitat community
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
 *  Last Update 24/06/2019
 *
 *
 *
 *  V4.5.0 - removed 'isStateChange: true' to reduce database load.
 *  V4.4.0 - Changed debug logging to reduce logging spam
 *  V4.3.0 - Added windPhraseForecast attribute
 *  V4.2.0 - Added 'currentIcon' attribute and ability to 'size' icons for dashboard display
 *  V4.1.0 - Made icons optional
 *  V4.0.0 - Reformatted and recoded to allow use with new WU api
 *  V3.1.0 - Added Icons for current and forecast weather for use with new tile app
 *  V3.0.0 - Updated info checking.
 *  V2.9.0 - Changed with way 'alerts' are handled for US/Non US timezones
 *  V2.8.1 - Debug Poll command
 *  V2.8.0 - Added switchable 'forecastIcon' to show current or forcast icon
 *  V2.7.0 - Added 'forecastIcon' for use with Sharptools
 *  V2.6.0 - Updated remote version checking
 *  V2.5.0 - Removed capabilities/attributes switch and reformatted all in lowercase - @Cobra 04/05/2018
 *  V2.4.1 - Debug - Changed the switchable capabilities to allow them to be seen by 'rule machine'- @Cobra 03/05/2018
 *  V2.4.0 - Added switchable 'Capabilities & Lowercase Data' for use with dashboards & Rule Machine - @Cobra 02/05/2018
 *  V2.3.0 - Added Moon phase and illumination percentage - @Cobra 01/05/2018
 *  V2.2.0 - Added 'Sunrise' and 'Sunset' - Thanks to: @Scottma61 for this one - @Cobra 01/05/2018
 *  V2.1.1 - Added defaultValue to "pollIntervalLimit" to prevent errors on new installs - @Cobra 01/05/2018
 *  V2.1.0 - Added 3 attributes - Rain tomorrow & the day after and Station_State also added poll counter and reset button @Cobra 01/05/2018
 *  V2.0.1 - Changed to one call to WU for Alerts, Conditions and Forecast - Thanks to: @Scottma61 for this one
 *  V2.0.0 - version alignment with lowercase version - @Cobra 27/04/2018 
 *  V1.9.0 - Added 'Chance_Of_Rain' an an attribute (also added to the summary) - @Cobra 27/04/2018 
 *  V1.8.0 - added 'stateChange' to some of the params that were not updating on poll unless changed - @Cobra 27/04/2018 
 *  V1.7.2 - Debug on lowercase version - updated version number for consistancy - @Cobra 26/04/2018 
 *  V1.7.1 - Debug - @Cobra 26/04/2018 
 *  V1.7.0 - Added 'Weather Summary' as a summary of the data with some English in between @Cobra - 26/04/2018
 *  V1.6.0 - Changed some attribute names - @Cobra - 25/04/2018/
 *  V1.5.0 - Added 'Station ID' so you can confirm you are using correct WU station @Cobra 25/04/2018
 *  V1.4.0 - Added ability to choose 'Pressure', 'Distance/Speed' & 'Precipitation' units & switchable logging- @Cobra 25/04/2018
 *  V1.3.0 - Added wind gust - removed some capabilities and added attributes - @Cobra 24/04/2018
 *  V1.2.0 - Added wind direction - @Cobra 23/04/2018
 *  V1.1.0 - Added ability to choose between "Fahrenheit" and "Celsius" - @Cobra 23/03/2018
 *  V1.0.0 - Original @mattw01 version
 *
 */

metadata {
    definition (name: "Custom WU Driver - New format", namespace: "Cobra", author: "Andrew Parker", importUrl: "https://raw.githubusercontent.com/CobraVmax/Hubitat/master/Drivers/Weather/WeatherUndergroundCustom.groovy") {
        capability "Actuator"
        capability "Sensor"
        capability "Temperature Measurement"
        capability "Illuminance Measurement"
        capability "Relative Humidity Measurement"
        
       

        
        command "poll"
        command "ForcePoll"
 	    command "ResetPollCount"
        attribute  "precipType", "string"
        attribute "solarradiation", "number"
        attribute "observation_time", "string"
 //       attribute "localSunrise", "string"
  //      attribute "localSunset", "string"
        attribute "weather", "string"
        attribute "feelsLike", "number"
		attribute "currentIcon", "string"
        attribute "forecastIcon", "string"
		attribute "city", "string"
        attribute "state", "string"
        attribute "percentPrecip", "string"
        attribute "wind_string", "string"
        attribute "pressure", "decimal"
        attribute "dewpoint", "number"
        attribute "visibility", "number"
        attribute "forecastHigh", "number"
        attribute "forecastLow", "number"
        attribute "forecastConditions", "string"
		attribute "currentConditions", "string"
        attribute "wind_dir", "string"
		attribute "wind_degree", "string"
        attribute "wind_gust", "string"
        attribute "precip_rate", "number"
        attribute "precip_today", "number"
        attribute "wind", "number"
		attribute "windPhrase", "string"
		attribute "windPhraseForecast", "string"
        attribute "UV", "number"
       	attribute "UVHarm", "string"
        attribute "pollsSinceReset", "number"
        attribute "temperatureUnit", "string"
        attribute "distanceUnit", "string"
        attribute "pressureUnit", "string"
        attribute "rainUnit", "string"
        attribute "summaryFormat", "string"
        attribute "alert", "string"
        attribute  "elevation", "number"
        attribute "stationID", "string"
		attribute "stationType", "sring"
        attribute "weatherSummary", "string"
        attribute "weatherSummaryFormat", "string"
        attribute "chanceOfRain", "string"
        attribute "rainTomorrow", "string"
        attribute "rainDayAfterTomorrow", "string"
        attribute "moonPhase", "string"
        attribute "moonIllumination", "string"
        attribute "latitude", "string"
		attribute "longitude", "string"
 		attribute "DriverAuthor", "string"
        attribute "DriverVersion", "string"
        attribute "DriverStatus", "string"
		attribute "DriverUpdate", "string"
		attribute "humidity", "string"
		
     
        
    }
    preferences() {
        section("Query Inputs"){
            input "apiKey", "text", required: true, title: "API Key"
            input "pollLocation", "text", required: true, title: "Station ID"
			input "unitFormat", "enum", required: true, title: "Unit Format",  options: ["Imperial", "Metric", "UK Hybrid1", "UK Hybrid2"]
			input "useIcons", "bool", required: false, title: "Use externally hosted icons (Optional)", defaultValue: false
			if(useIcons){
			input "iconURL1", "text", required: true, title: "Icon Base URL"
			input "iconHeight1", "text", required: true, title: "Icon Height", defaultValue: 25
			input "iconWidth1", "text", required: true, title: "Icon Width", defaultValue: 25}			
            input "pollIntervalLimit", "number", title: "Poll Interval Limit:", required: true, defaultValue: 1
            input "autoPoll", "bool", required: false, title: "Enable Auto Poll"
            input "pollInterval", "enum", title: "Auto Poll Interval:", required: false, defaultValue: "5 Minutes", options: ["5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
            input "logSet", "bool", title: "Enable Logging", required: true, defaultValue: false
            input "cutOff", "time", title: "New Day Starts", required: true
			
        }
    }
}

def updated() {
    log.debug "updated called"
   updateCheck()
    unschedule()
    version()
    state.NumOfPolls = 0
    ForcePoll()
    def pollIntervalCmd = (settings?.pollInterval ?: "5 Minutes").replace(" ", "")
    if(autoPoll)
        "runEvery${pollIntervalCmd}"(pollSchedule)
    
     def changeOver = cutOff
    schedule(changeOver, ResetPollCount)

}

def ResetPollCount(){
state.NumOfPolls = -1
    log.info "Poll counter reset.."
ForcePoll()
}

def pollSchedule()
{
    ForcePoll()
}
              
def parse(String description) {
}

def poll()
{
    if(now() - state.lastPoll > (pollIntervalLimit * 60000))
        ForcePoll()
    else
        log.debug "Poll called before interval threshold was reached"
}



def formatUnit(){
	if(unitFormat == "Imperial"){
		state.unit = "e"
		log.info "state.unit = $state.unit"
	}
	if(unitFormat == "Metric"){
		state.unit = "m"
		log.info "state.unit = $state.unit"
	}
	if(unitFormat == "UK Hybrid1" || unitFormat == "UK Hybrid2" ){
		state.unit = "h"
		log.info "state.unit = $state.unit"
	}
	
	
}
def ForcePoll(){
	poll1()
	poll2()
	
}
	
	
	
	
def poll1(){
	
    formatUnit()
    state.NumOfPolls = (state.NumOfPolls) + 1
    log.info " state.NumOfPolls = $state.NumOfPolls" 
   
    log.debug "WU: ForcePoll called"
    def params1 = [
		// Current Observation
       uri: "https://api.weather.com/v2/pws/observations/current?stationId=${pollLocation}&format=json&units=${state.unit}&apiKey=${apiKey}"
    ]

    try {
        httpGet(params1) { resp1 ->
            resp1.headers.each {
                if(logSet == true){log.debug "Response1: ${it.name} : ${it.value}"}
        }
            if(logSet == true){  
           
            log.debug "params1: ${params1}"
            log.debug "response contentType: ${resp1.contentType}"
 		    log.debug "response data: ${resp1.data}"
            } 
            if(logSet == false){ 
            log.info "Further WU detailed data logging disabled"    
            }    
 
    		def illume = (resp1.data.observations.solarRadiation[0])
            if(illume){
            	 sendEvent(name: "illuminance", value: resp1.data.observations.solarRadiation[0], unit: "lux")
                 sendEvent(name: "solarradiation", value: resp1.data.observations.solarRadiation[0], unit: "W")
            }
            if(!illume){
                 sendEvent(name: "illuminance", value: "No Data")
            	 sendEvent(name: "solarradiation", value: "No Data")
            }   
            sendEvent(name: "pollsSinceReset", value: state.NumOfPolls)
            sendEvent(name: "stationID", value: resp1.data.observations.stationID[0])
			sendEvent(name: "stationType", value: resp1.data.observations.softwareType[0])
			
			
			
			sendEvent(name: "humidity", value: resp1.data.observations.humidity[0])
            sendEvent(name: "observation_time", value: resp1.data.observations.obsTimeLocal[0])
            sendEvent(name: "wind_degree", value: resp1.data.observations.winddir[0])			
			state.latt1 = (resp1.data.observations.lat[0])
			state.long1 = (resp1.data.observations.lon[0])
			sendEvent(name: "latitude", value: state.latt1)
			sendEvent(name: "longitude", value: state.long1)	
            
			if(unitFormat == "Imperial"){
			sendEvent(name: "precip_rate", value: resp1.data.observations.imperial.precipRate[0], unit: "in")
            sendEvent(name: "precip_today", value: resp1.data.observations.imperial.precipTotal[0], unit: "in")
			sendEvent(name: "feelsLike", value: resp1.data.observations.imperial.windChill[0], unit: "F")   
            sendEvent(name: "temperature", value: resp1.data.observations.imperial.temp[0], unit: "F")
			sendEvent(name: "wind", value: resp1.data.observations.imperial.windSpeed[0], unit: "mph")
            sendEvent(name: "wind_gust", value: resp1.data.observations.imperial.windGust[0]) 
			sendEvent(name: "dewpoint", value: resp1.data.observations.imperial.dewpt[0], unit: "F")
			sendEvent(name: "pressure", value: resp1.data.observations.imperial.pressure[0])
			sendEvent(name: "elevation", value: resp1.data.observations.imperial.elev[0])
			}
			if(unitFormat == "Metric"){
			sendEvent(name: "precip_rate", value: resp1.data.observations.metric.precipRate[0], unit: "mm")
            sendEvent(name: "precip_today", value: resp1.data.observations.metric.precipTotal[0], unit: "mm")
			sendEvent(name: "feelsLike", value: resp1.data.observations.metric.windChill[0], unit: "C")   
            sendEvent(name: "temperature", value: resp1.data.observations.metric.temp[0], unit: "C")
			sendEvent(name: "wind", value: resp1.data.observations.metric.windSpeed[0], unit: "kph")
            sendEvent(name: "wind_gust", value: resp1.data.observations.metric.windGust[0]) 
			sendEvent(name: "dewpoint", value: resp1.data.observations.metric.dewpt[0], unit: "C")
			sendEvent(name: "pressure", value: resp1.data.observations.metric.pressure[0])	
			sendEvent(name: "elevation", value: resp1.data.observations.metric.elev[0])
			}
			if(unitFormat == "UK Hybrid1"){
			sendEvent(name: "precip_rate", value: resp1.data.observations.uk_hybrid.precipRate[0], unit: "in")
            sendEvent(name: "precip_today", value: resp1.data.observations.uk_hybrid.precipTotal[0], unit: "in")
			sendEvent(name: "feelsLike", value: resp1.data.observations.uk_hybrid.windChill[0], unit: "C")   
            sendEvent(name: "temperature", value: resp1.data.observations.uk_hybrid.temp[0], unit: "C")
			sendEvent(name: "wind", value: resp1.data.observations.uk_hybrid.windSpeed[0], unit: "mph")
            sendEvent(name: "wind_gust", value: resp1.data.observations.uk_hybrid.windGust[0]) 
			sendEvent(name: "dewpoint", value: resp1.data.observations.uk_hybrid.dewpt[0], unit: "C")
			sendEvent(name: "pressure", value: resp1.data.observations.uk_hybrid.pressure[0])
			sendEvent(name: "elevation", value: resp1.data.observations.uk_hybrid.elev[0])
			}
			
			
			if(unitFormat == "UK Hybrid2"){
			sendEvent(name: "precip_rate", value: resp1.data.observations.metric.precipRate[0], unit: "mm")
            sendEvent(name: "precip_today", value: resp1.data.observations.metric.precipTotal[0], unit: "mm")	
			sendEvent(name: "feelsLike", value: resp1.data.observations.uk_hybrid.windChill[0], unit: "C")   
            sendEvent(name: "temperature", value: resp1.data.observations.uk_hybrid.temp[0], unit: "C")
			sendEvent(name: "wind", value: resp1.data.observations.uk_hybrid.windSpeed[0], unit: "mph")
            sendEvent(name: "wind_gust", value: resp1.data.observations.uk_hybrid.windGust[0]) 
			sendEvent(name: "dewpoint", value: resp1.data.observations.uk_hybrid.dewpt[0], unit: "C")
			sendEvent(name: "pressure", value: resp1.data.observations.uk_hybrid.pressure[0])
			sendEvent(name: "elevation", value: resp1.data.observations.uk_hybrid.elev[0])
			}
			
			
			
			state.lastPoll = now()
        } 
       } catch (e) {
        log.error "something went wrong in Poll1 : $e"
    }
    
}

 
   

/////////////////////////////////////////////////////// POLL 2 /////////////////////////////////////////////////////////////////////////////////////////////////




// Forecast Stuff
def poll2(){
	 def params2 = [
	//	Forecast
	uri: "https://api.weather.com/v3/wx/forecast/daily/5day?geocode=${state.latt1},${state.long1}&units=${state.unit}&language=en-GB&format=json&apiKey=${apiKey}"
    ]
	
    try {
        httpGet(params2) { resp2 ->
            resp2.headers.each {
                if(logSet == true){log.debug "Response2: ${it.name} : ${it.value}"}
        }
            if(logSet == true){  
           
            log.debug "params2: ${params2}"
            log.debug "response contentType: ${resp2.contentType}"
 		    log.debug "response data: ${resp2.data}"
            } 
            if(logSet == false){ 
            log.info "Further WU forecast detailed data logging disabled"    
            }    
            sendEvent(name: "precipType", value: resp2.data.daypart[0].precipType[0])
            sendEvent(name: "chanceOfRain", value: resp2.data.daypart[0].precipChance[0])
			
			sendEvent(name: "rainTomorrow", value: resp2.data.daypart[0].qpf[0])
			
			sendEvent(name: "currentConditions", value: resp2.data.narrative[0])
			sendEvent(name: "forecastConditions", value: resp2.data.narrative[1])
			sendEvent(name: "weather", value: resp2.data.daypart[0].wxPhraseLong[0])
			sendEvent(name: "wind_dir", value: resp2.data.daypart[0].windDirectionCardinal[0])
			sendEvent(name: "windPhrase", value: resp2.data.daypart[0].windPhrase[0])
			sendEvent(name: "windPhraseForecast", value: resp2.data.daypart[0].windPhrase[1])

			
			sendEvent(name: "forecastHigh", value: resp2.data.temperatureMax[0])
			sendEvent(name: "forecastLow", value: resp2.data.temperatureMin[0])
			sendEvent(name: "moonPhase", value: resp2.data.moonPhase[0])
			sendEvent(name: "UVHarm", value: resp2.data.daypart[0].uvDescription[0]) 
			
			state.dayOrNight = (resp2.data.daypart[0].dayOrNight[0])
	//		 log.warn "day/night is $state.dayOrNight"
			if(useIcons){
				if(state.dayOrNight == "D" || state.dayOrNight == null){	
			state.iconCode1 = (resp2.data.daypart[0].iconCode[0])
			state.iconCode2 = (resp2.data.daypart[0].iconCode[2])	
				}				
			if(state.dayOrNight == "N"){	
			state.iconCode1 = (resp2.data.daypart[0].iconCode[2])
			state.iconCode2 = (resp2.data.daypart[0].iconCode[3])	
				}			
			state.icon1 = "<img src='" +iconURL1 +state.iconCode1 +".png" +"' width='" +iconWidth1 +"' height='" +iconHeight1 +"'>"
			state.icon2 = "<img src='" +iconURL1 +state.iconCode2 +".png" +"' width='" +iconWidth1 +"' height='" +iconHeight1 +"'>"
			sendEvent(name: "currentIcon", value: state.icon1) 
			sendEvent(name: "forecastIcon", value: state.icon2) 
			} 
        	state.lastPoll = now()     

        } 
        
    } catch (e) {
        log.error "something went wrong in Poll 2: $e"
    }

}




// 

















def Report(){
  def obvTime = Observation_Time.value
    
  log.info "$obvTime"  
    
}


def version(){
    updateCheck()
   schedule("0 0 9 ? * FRI *", updateCheck)
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
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = "Updated: "+state.newUpdateDate + " - "+(respUD.data.versions.UpdateInfo.Driver.(state.InternalName))
            state.author = (respUD.data.author)
			state.newUpdateDate = (respUD.data.Comment)
           
		if(newVer == "NLS"){
            state.Status = "<b>** This driver is no longer supported by $state.author  **</b>"       
            log.warn "** This driver is no longer supported by $state.author **"      
      		}           
		else if(currentVer < newVer){
        	state.Status = "<b>New Version Available (Version: $newVerRaw)</b>"
        	log.warn "** There is a newer version of this driver available  (Version: $newVerRaw) **"
        	log.warn "** $state.UpdateInfo **"
       		}
		else if(currentVer > newVer){
        	state.Status = "You are using a BETA (Version: $state.version)"
        	log.warn "** <b>You are using a BETA (Version: $state.version)</b>) **"
        	state.UpdateInfo = "N/A"
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
    		sendEvent(name: "DriverVersion", value: state.version, isStateChange: true)
    
    
    	//	
}

def setVersion(){
    state.version = "4.5.0"
    state.InternalName = "WUWeatherDriver"
   	state.CobraAppCheck = "customwu.json"
    sendEvent(name: "DriverAuthor", value: "Cobra", isStateChange: true)
    sendEvent(name: "DriverVersion", value: state.version, isStateChange: true)
    
    
      
}
