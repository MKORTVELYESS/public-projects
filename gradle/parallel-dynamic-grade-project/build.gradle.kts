//call to achieve dynamic flagging of commands like
    // ./gradlew sitePreparation -PsiteClearanceFlags="-d 2024-09-28" -PlandSurveyFlags="--date 2024.09.28" -PsitePreparationFlags="-D 20240928"

tasks.register("sitePreparation") {//If paralellism work should take ~12 seconds
    val flagsProvider: Provider<String> = project.providers.gradleProperty("sitePreparationFlags")
    dependsOn(landSurvey, siteClearance) //site preparation depends on landSurvey and siteClearance which can happen paralelly to save time
    doLast {
        println("Site preparation in progress: Land surveying and site clearance for ${flagsProvider.getOrElse("")}")
        // Add any additional logic here, such as simulating site preparation actions.
    }
}
val landSurvey by tasks.registering {//Takes 11 seconds
    val flagsProvider: Provider<String> = project.providers.gradleProperty("landSurveyFlags")

    doLast {
        println("Land survey started ${flagsProvider.getOrElse("")}")
        Thread.sleep(10000)
        println("Land survey at 90%.")
        Thread.sleep(1000)
        println("Land survey complete")
    }
}

val siteClearance by tasks.registering { //Takes 11  seconds
    val flagsProvider: Provider<String> = project.providers.gradleProperty("siteClearanceFlags")

    doLast {
        println("Site clearance started ${flagsProvider.getOrElse("")}")
        Thread.sleep(10000)
        println("Site clearance at 90%.")
        Thread.sleep(1000)
        println("Site complete.")
    }
}


