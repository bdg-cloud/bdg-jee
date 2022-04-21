#!/usr/bin/ruby

require 'rest-client'
require 'json'

dateDeb = '2017-06-12'
dateFin = '2017-06-23'


#####################################################################################################

#dateDeb = ARGV[0]
#dateFin = ARGV[1]
#resolution = ARGV[2]

# svn log svn+ssh://192.168.1.12/data3/prog/svn/branches/b_2_0_13_JEE -r 20800:20900 -v

#product=TestProduct"

#####################################################################################################

def dateBuild(nomJob)
	RestClient.get 'http://192.168.1.12:8080/hudson/view/JEE/job/'+nomJob+'/lastSuccessfulBuild/api/xml', 
	{:params => 
	{'xpath' => '/*/id/text()'}
	}
end

#####################################################################################################

def changelog(deb,fin,fich)

	resolution = 'FIXED'

	response = RestClient.get 'http://bugs.legrain.fr/rest/bug', 
	{:params => 
	{'api_key' => 'zpV28PVz133uhN8aLHaDKOtRThw3nxc8UMExC6JW',
	 'chfieldfrom' => deb,
	 'chfieldto' => fin,
	 'include_fields' => 'id,summary,last_change_time,status,severity,priority,resolution',
	 'resolution' => resolution
	}
	}

	File.open(fich, 'w') { |file| 
		json = JSON.parse(response)
		#puts JSON.pretty_generate(json)

		json["result"]["bugs"].each { |b| 
			puts b["id"] 
			#file.puts(b["id"]) 
			#file.puts(b["summary"]) 
			file.puts(b["id"]+';'+b["summary"]) 
		}
	}
end

#####################################################################################################
# Récupération des différentes dates de build à partir des jobs Hudson
dateDernierBuildDevLocal = dateBuild('solstyce')
dateDernierBuildDevInternet = dateBuild('solstyce%20test%20dev%20web')
dateDernierBuildPProd = dateBuild('solstyce%20test%20QA%20web')
dateDernierBuildProd = dateBuild('solstyce%20production')

timestamp = ' [{"dateDernierBuildDevLocal" : "'+dateDernierBuildDevLocal+'"},
	{"dateDernierBuildDevInternet" : "'+dateDernierBuildDevInternet+'"},
	{"dateDernierBuildPProd" : "'+dateDernierBuildPProd+'"},
	{"dateDernierBuildProd" : "'+dateDernierBuildProd+'"},
	{"dateBuild" : "'+Time.now.strftime("%Y-%m-%d_%H-%M-%S").to_s+'"}
	]'
json_timestamp = JSON.parse(timestamp)
puts JSON.pretty_generate(json_timestamp)

File.open('svn/fr.legrain.bdg.webapp/src/main/resources/META-INF/resources/changelog/timestamps.txt', 'w') { |file| 
	file.puts(json_timestamp) 
}
#####################################################################################################

#dateDeb = Time.strptime("12/22/2011", "%m/%d/%Y")
dateDeb = DateTime.strptime(dateDernierBuildDevLocal, '%Y-%m-%d_%H-%M-%S')
dateDeb = dateDeb - 20
puts dateDeb
dateDeb = dateDeb.strftime("%Y-%m-%d")
dateFin = Time.now
puts dateDeb

changelog(dateDeb,dateFin,'svn/fr.legrain.bdg.webapp/src/main/resources/META-INF/resources/changelog/changelog.txt')

dateDeb = DateTime.strptime(dateDernierBuildDevInternet, '%Y-%m-%d_%H-%M-%S')
dateDeb = dateDeb.strftime("%Y-%m-%d")
changelog(dateDeb,dateFin,'svn/fr.legrain.bdg.webapp/src/main/resources/META-INF/resources/changelog/changelog_dev_internet.txt')

dateDeb = DateTime.strptime(dateDernierBuildPProd, '%Y-%m-%d_%H-%M-%S')
dateDeb = dateDeb.strftime("%Y-%m-%d")
changelog(dateDeb,dateFin,'svn/fr.legrain.bdg.webapp/src/main/resources/META-INF/resources/changelog/changelog_pprod.txt')

dateDeb = DateTime.strptime(dateDernierBuildProd, '%Y-%m-%d_%H-%M-%S')
dateDeb = dateDeb.strftime("%Y-%m-%d")
changelog(dateDeb,dateFin,'svn/fr.legrain.bdg.webapp/src/main/resources/META-INF/resources/changelog/changelog_prod.txt')

#####################################################################################################


