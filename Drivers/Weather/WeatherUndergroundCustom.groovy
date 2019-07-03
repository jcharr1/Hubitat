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
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Last Update 02/07/2019
 *
 *  V4.6.1 - Added more tile friendly attributes - @jcharr1 02/07/2019
 *  V4.6.0 - Converted httpGet call to asynchttpGet
 *  This should prevent hub waiting for the respose from WU
 *  Randomised the update check routine to reduce load on my update server.
 *  Auto turn off logging after 30 minutes
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
	definition (name: "Custom WU Driver", namespace: "Cobra", author: "Andrew Parker", importUrl: "https://raw.githubusercontent.com/jcharr1/Hubitat/master/Drivers/Weather/WeatherUndergroundCustom.groovy") {
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
		//attribute "localSunrise", "string"
		//attribute "localSunset", "string"
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
		
		// Dashboard tile friendly attributes
		attribute "WindAndDir", "string" // from PWS
		attribute "PrecipAmount", "string"
		attribute "PrecipRate", "string"
		attribute "PrecipChance", "string"
		attribute "TempAndHumidity", "string"
		attribute "FeelsLike", "string"
		attribute "Barometer", "string"
	}
	
	preferences() {
		section("Query Inputs") {
			input "apiKey", "text", required: true, title: "API Key"
			input "pollLocation", "text", required: true, title: "Station ID"
			input "unitFormat", "enum", required: true, title: "Unit Format",  options: ["Imperial", "Metric", "UK Hybrid"]
			input "useIcons", "bool", required: false, title: "Use externally hosted icons (Optional)", defaultValue: false
			if(useIcons) {
				input "iconURL1", "text", required: true, title: "Icon Base URL"
				input "iconHeight1", "text", required: true, title: "Icon Height", defaultValue: 25
				input "iconWidth1", "text", required: true, title: "Icon Width", defaultValue: 25
			}			
			input "pollIntervalLimit", "number", title: "Poll Interval Limit:", required: true, defaultValue: 1
			input "autoPoll", "bool", required: false, title: "Enable Auto Poll"
			input "pollInterval", "enum", title: "Auto Poll Interval:", required: false, defaultValue: "5 Minutes", options: ["1 Minute", "5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
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
	if(logSet) {runIn(1800, logsOff)}
}

def ResetPollCount() {
	state.NumOfPolls = -1
		log.info "Poll counter reset.."
	ForcePoll()
}

def pollSchedule() {
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



def formatUnit() {
	if(unitFormat == "Imperial") {
		state.unit = "e"
		if(logSet == true) {log.info "state.unit = $state.unit"}
	}
	if(unitFormat == "Metric") {
		state.unit = "m"
		if(logSet == true) {log.info "state.unit = $state.unit"}
	}
	if(unitFormat == "UK Hybrid") {
		state.unit = "h"
		if(logSet == true) {log.info "state.unit = $state.unit"}
	}	
}

def ForcePoll() {
	if(logSet == true) {log.debug "WU: Poll called"}
	state.NumOfPolls = (state.NumOfPolls) + 1
	poll1()
	poll2()	
}
	
def pollHandler1(resp, data) {
	if(resp.getStatus() == 200 || resp.getStatus() == 207) {
		obs = parseJson(resp.data)
		if(logSet == true) {log.debug "Response Data1 = $obs"}		// log the data returned by WU
		if(logSet == false) {log.info "Further logging disabled"}
		def illume = (obs.observations.solarRadiation[0])
		if(illume) {
			sendEvent(name: "illuminance", value: obs.observations.solarRadiation[0], unit: "lux")
			sendEvent(name: "solarradiation", value: obs.observations.solarRadiation[0], unit: "W")
		}
		if(!illume) {
			sendEvent(name: "illuminance", value: "No Data")
			sendEvent(name: "solarradiation", value: "No Data")
		}   
		sendEvent(name: "pollsSinceReset", value: state.NumOfPolls)
		sendEvent(name: "stationID", value: obs.observations.stationID[0])
		sendEvent(name: "stationType", value: obs.observations.softwareType[0])
		sendEvent(name: "humidity", value: obs.observations.humidity[0], unit: "%")
		sendEvent(name: "observation_time", value: obs.observations.obsTimeLocal[0])
		sendEvent(name: "wind_degree", value: obs.observations.winddir[0])			
		state.latt1 = (obs.observations.lat[0])
		state.long1 = (obs.observations.lon[0])
		sendEvent(name: "latitude", value: state.latt1)
		sendEvent(name: "longitude", value: state.long1)	
		if(unitFormat == "Imperial") {
			sendEvent(name: "precip_rate", value: obs.observations.imperial.precipRate[0])
			sendEvent(name: "PrecipRate", value: "🌧" + obs.observations.imperial.precipRate[0] + '"/hr')
			sendEvent(name: "precip_today", value: obs.observations.imperial.precipTotal[0])
			sendEvent(name: "PrecipAmount", value: "🌧" + obs.observations.imperial.precipTotal[0] + '"')
			sendEvent(name: "feelsLike", value: obs.observations.imperial.windChill[0], unit: "℉")  
			def feelsLikeTemp = obs.observations.imperial.heatIndex[0]
			def feelsLikeEmoji = "☀️"
			if(obs.observations.imperial.temp[0] <= 61) {
				feelsLikeTemp = obs.observations.imperial.windChill[0]
				feelsLikeEmoji = "❄️"
			}
			sendEvent(name: "FeelsLike", value: "Feels Like" + feelsLikeEmoji + feelsLikeTemp + "℉")
			sendEvent(name: "temperature", value: obs.observations.imperial.temp[0], unit: "℉")
			sendEvent(name: "TempAndHumidity", value: obs.observations.imperial.temp[0] + "℉🌡 " + obs.observations.humidity[0] + "%💧")
			sendEvent(name: "wind", value: obs.observations.imperial.windSpeed[0], unit: "mph")
			def compassDir = degreesToCompass(obs.observations.winddir[0])
			sendEvent(name: "WindAndDir", value: "💨" + compassDir + " " + obs.observations.imperial.windSpeed[0] + "|" + obs.observations.imperial.windGust[0] + "mph")
			sendEvent(name: "wind_gust", value: obs.observations.imperial.windGust[0]) 
			sendEvent(name: "dewpoint", value: obs.observations.imperial.dewpt[0], unit: "℉")
			sendEvent(name: "pressure", value: obs.observations.imperial.pressure[0])
			sendEvent(name: "Barometer", value: "🧭" + obs.observations.imperial.pressure[0] + "inHg")
			sendEvent(name: "elevation", value: obs.observations.imperial.elev[0])
		}
		if(unitFormat == "Metric") {
			sendEvent(name: "precip_rate", value: obs.observations.metric.precipRate[0])
			sendEvent(name: "PrecipRate", value: "🌧" + obs.observations.metric.precipRate[0] + 'mm/hr')
			sendEvent(name: "precip_today", value: obs.observations.metric.precipTotal[0])
			sendEvent(name: "PrecipAmount", value: "🌧" + obs.observations.metric.precipTotal[0] + 'mm')
			sendEvent(name: "feelsLike", value: obs.observations.metric.windChill[0], unit: "℃")   
			def feelsLikeTemp = obs.observations.metric.heatIndex[0]
			def feelsLikeEmoji = "☀️"
			if(obs.observations.metric.temp[0] <= 16) {
				feelsLikeTemp = obs.observations.metric.windChill[0]
				feelsLikeEmoji = "❄️"
			}
			sendEvent(name: "FeelsLike", value: "Feels Like" + feelsLikeEmoji + feelsLikeTemp + "℃")
			sendEvent(name: "temperature", value: obs.observations.metric.temp[0], unit: "℃")
			sendEvent(name: "TempAndHumidity", value: obs.observations.metric.temp[0] + "℃🌡 " + obs.observations.humidity[0] + "%💧")
			sendEvent(name: "wind", value: obs.observations.metric.windSpeed[0], unit: "kph")
			def compassDir = degreesToCompass(obs.observations.winddir[0])
			sendEvent(name: "WindAndDir", value: "💨" + compassDir + " " + obs.observations.metric.windSpeed[0] + "|" + obs.observations.metric.windGust[0] + "kph")
			sendEvent(name: "wind_gust", value: obs.observations.metric.windGust[0]) 
			sendEvent(name: "dewpoint", value: obs.observations.metric.dewpt[0], unit: "℃")
			sendEvent(name: "pressure", value: obs.observations.metric.pressure[0])	
			sendEvent(name: "Barometer", value: "🧭" + obs.observations.metric.pressure[0] + "hPa")
			sendEvent(name: "elevation", value: obs.observations.metric.elev[0])
		}
		if(unitFormat == "UK Hybrid") {
			sendEvent(name: "precip_rate", value: obs.observations.uk_hybrid.precipRate[0])
			sendEvent(name: "PrecipRate", value: "🌧" + obs.observations.uk_hybrid.precipRate[0] + 'mm/hr')
			sendEvent(name: "precip_today", value: obs.observations.uk_hybrid.precipTotal[0])
			sendEvent(name: "PrecipAmount", value: "🌧" + obs.observations.uk_hybrid.precipTotal[0] + 'mm')
			sendEvent(name: "feelsLike", value: obs.observations.uk_hybrid.windChill[0], unit: "℃")   
			def feelsLikeTemp = obs.observations.uk_hybrid.heatIndex[0]
			def feelsLikeEmoji = "☀️"
			if(obs.observations.uk_hybrid.temp[0] <= 16) {
				feelsLikeTemp = obs.observations.uk_hybrid.windChill[0]
				feelsLikeEmoji = "❄️"
			}
			sendEvent(name: "FeelsLike", value: "Feels Like" + feelsLikeEmoji + feelsLikeTemp + "℃")
			sendEvent(name: "temperature", value: obs.observations.uk_hybrid.temp[0], unit: "℃")
			sendEvent(name: "TempAndHumidity", value: obs.observations.uk_hybrid.temp[0] + "℃🌡 " + obs.observations.humidity[0] + "%💧")
			sendEvent(name: "wind", value: obs.observations.uk_hybrid.windSpeed[0], unit: "mph")
			def compassDir = degreesToCompass(obs.observations.winddir[0])
			sendEvent(name: "WindAndDir", value: "💨" + compassDir + " " + obs.observations.uk_hybrid.windSpeed[0] + "|" + obs.observations.uk_hybrid.windGust[0] + "mph")
			sendEvent(name: "wind_gust", value: obs.observations.uk_hybrid.windGust[0]) 
			sendEvent(name: "dewpoint", value: obs.observations.uk_hybrid.dewpt[0], unit: "C")
			sendEvent(name: "pressure", value: obs.observations.uk_hybrid.pressure[0])
			sendEvent(name: "Barometer", value: "🧭" + obs.observations.uk_hybrid.pressure[0] + "hPa")
			sendEvent(name: "elevation", value: obs.observations.uk_hybrid.elev[0])
		}
			
		state.lastPoll = now()		
	
	} else {
		def res = resp.getStatus()
		log.error "WU weather api did not return data from poll1 - $res"
	}
}
	
def poll1() {
	formatUnit()  
	def params1 = [
		uri: "https://api.weather.com/v2/pws/observations/current?stationId=${pollLocation}&format=json&units=${state.unit}&apiKey=${apiKey}"
	 //   uri: "https://api.weather.com/v2/pws/observations/current?stationId=IDURHAM16&format=json&units=${state.unit}&apiKey=${apiKey}"
	]
	asynchttpGet("pollHandler1", params1)   
}
		   
def poll2() {
	formatUnit()
	def params2 = [uri: "https://api.weather.com/v3/wx/forecast/daily/5day?geocode=${state.latt1},${state.long1}&units=${state.unit}&language=en-GB&format=json&apiKey=${apiKey}"]
	asynchttpGet("pollHandler2", params2)
}

def pollHandler2(resp1, data) {
	if(resp1.getStatus() == 200 || resp1.getStatus() == 207) {
		obs1 = parseJson(resp1.data)
		if(logSet == true) {log.debug "Response Data2 = $obs1"}		// log the data returned by WU
		sendEvent(name: "precipType", value: obs1.daypart[0].precipType[0])
   
		sendEvent(name: "chanceOfRain", value: obs1.daypart[0].precipChance[0])
		def precip_chance = obs1.daypart[0].precipChance[0]
		if(!precip_chance) {
			precip_chance = 0
		}
		sendEvent(name: "PrecipChance", value: "🌧" + precip_chance + "% Chance")
		sendEvent(name: "rainTomorrow", value: obs1.daypart[0].qpf[0])
		sendEvent(name: "currentConditions", value: obs1.narrative[0])
		sendEvent(name: "forecastConditions", value: obs1.narrative[1])
		sendEvent(name: "weather", value: obs1.daypart[0].wxPhraseLong[0])
		sendEvent(name: "wind_dir", value: obs1.daypart[0].windDirectionCardinal[0])
		sendEvent(name: "windPhrase", value: obs1.daypart[0].windPhrase[0])
		sendEvent(name: "windPhraseForecast", value: obs1.daypart[0].windPhrase[1])
		sendEvent(name: "forecastHigh", value: obs1.temperatureMax[0])
		sendEvent(name: "forecastLow", value: obs1.temperatureMin[0])
		sendEvent(name: "moonPhase", value: obs1.moonPhase[0])
		sendEvent(name: "UVHarm", value: obs1.daypart[0].uvDescription[0]) 
		state.dayOrNight = (obs1.daypart[0].dayOrNight[0])
		//log.warn "day/night is $state.dayOrNight"
		if(useIcons) {
			if(state.dayOrNight == "D" || state.dayOrNight == null) {	
				state.iconCode1 = (obs1.daypart[0].iconCode[0])
				state.iconCode2 = (obs1.daypart[0].iconCode[2])	
			}				
			if(state.dayOrNight == "N") {	
				state.iconCode1 = (obs1.daypart[0].iconCode[2])
				state.iconCode2 = (obs1.daypart[0].iconCode[3])	
			}			
			state.icon1 = "<img src='" +iconURL1 +state.iconCode1 +".png" +"' width='" +iconWidth1 +"' height='" +iconHeight1 +"'>"
			state.icon2 = "<img src='" +iconURL1 +state.iconCode2 +".png" +"' width='" +iconWidth1 +"' height='" +iconHeight1 +"'>"
			sendEvent(name: "currentIcon", value: state.icon1) 
			sendEvent(name: "forecastIcon", value: state.icon2) 
		} 
	} 
}

def logsOff() {
	log.warn "Debug logging disabled..."
	device.updateSetting("logSet", [value: "false", type: "bool"])
}

def version() {
	updateCheck()
   	def random = new Random()
	Integer randomHour = random.nextInt(18-10) + 10
	Integer randomDayOfWeek = random.nextInt(7-1) + 1 
	schedule("0 0 " + randomHour + " ? * " + randomDayOfWeek, updateCheck) 
}

def updateCheck() {
	setVersion()
	def paramsUD = [uri: "http://update.hubitat.uk/json/${state.CobraAppCheck}"] 
	try {
		httpGet(paramsUD) { respUD ->
			//  log.warn " Version Checking - Response Data: ${respUD.data}"// Troubleshooting Debug Code **********************
			def copyrightRead = (respUD.data.copyright)
			state.Copyright = copyrightRead
			def newVerRaw = (respUD.data.versions.Driver.(state.InternalName))
			//log.warn "$state.InternalName = $newVerRaw"
			def newVer = newVerRaw.replace(".", "")
			//log.warn "$state.InternalName = $newVer"
			def currentVer = state.version.replace(".", "")
			state.UpdateInfo = "Updated: "+state.newUpdateDate + " - "+(respUD.data.versions.UpdateInfo.Driver.(state.InternalName))
			state.author = (respUD.data.author)
			state.newUpdateDate = (respUD.data.Comment)

			if(newVer == "NLS") {
				state.Status = "<b>** This driver is no longer supported by $state.author  **</b>"	   
				log.warn "** This driver is no longer supported by $state.author **"	  
			}		   
			else if(currentVer < newVer) {
				state.Status = "<b>New Version Available (Version: $newVerRaw)</b>"
				log.warn "** There is a newer version of this driver available  (Version: $newVerRaw) **"
				log.warn "** $state.UpdateInfo **"
			}
			else if(currentVer > newVer) {
				state.Status = "You are using a BETA (Version: $state.version)"
				log.warn "** <b>You are using a BETA (Version: $state.version)</b>) **"
				state.UpdateInfo = "N/A"
			} 	
			else { 
				state.Status = "Current"
				log.info "You are using the current version of this driver"
			}
		}
	} 
	catch (e) {
		log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
	}
	if(state.Status == "Current") {
		state.UpdateInfo = "N/A"
		sendEvent(name: "DriverUpdate", value: state.UpdateInfo)
		sendEvent(name: "DriverStatus", value: state.Status)
	}
	else{
		sendEvent(name: "DriverUpdate", value: state.UpdateInfo)
		sendEvent(name: "DriverStatus", value: state.Status)
	}   
	sendEvent(name: "DriverAuthor", value: state.author)
	sendEvent(name: "DriverVersion", value: state.version)
}

def setVersion() {
	state.version = "4.6.0"
	state.InternalName = "WUWeatherDriver"
	state.CobraAppCheck = "customwu.json"
	sendEvent(name: "DriverAuthor", value: "Cobra")
	sendEvent(name: "DriverVersion", value: state.version)  
}

def degreesToCompass(degrees) {
	def compass = ["N","NNE","NE","ENE","E","ESE","SE","SSE","S","SSW","SW","WSW","W","WNW","NW","NNW","N"]
	def arrows = ["⬇︎","⬋","⬅︎","⬉","⬆︎","⬈","➡︎","⬊","⬇︎"]
	int index = Math.round((degrees % 360) / 22.5)
	int arrowIndex = Math.round((degrees % 360) / 45)
	log.debug "index = " + index
	log.debug "arrowIndex = " + arrowIndex
	return(arrows[arrowIndex] + compass[index])
}