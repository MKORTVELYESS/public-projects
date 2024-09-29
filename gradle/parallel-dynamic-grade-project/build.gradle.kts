//call to achieve dynamic flagging of commands like
// ./gradlew sitePreparation -PsiteClearanceFlags="-d 2024-09-28" -PlandSurveyFlags="--date 2024.09.28" -PsitePreparationFlags="-D 20240928"
// Exclude some tasks which would normally be triggered by dependency: ./gradlew sitePreparation -x siteClearance -PsiteClearanceFlags="-d 2024-09-28" -PlandSurveyFlags="--date 2024.09.28" -PsitePreparationFlags="-D 20240928"

abstract class FlaggedTask : DefaultTask() {
    @Input // Shared property for all tasks to use, needs to marked as input https://docs.gradle.org/8.10/userguide/validation_problems.html#missing_annotation
    val flagsProvider: Provider<String> = project.providers.gradleProperty("${name}Flags")
}

val sitePreparation by tasks.registering(FlaggedTask::class) {//If paralellism work should take ~12 seconds
    dependsOn(landSurvey, siteClearance) //site preparation depends on landSurvey and siteClearance which can happen paralelly to save time
    doLast {
        println("Site preparation in progress: Land surveying and site clearance for ${flagsProvider.getOrElse("")}")
    }
}

val landSurvey by tasks.registering(FlaggedTask::class) {//Takes 11 seconds
    doLast {
        println("Land survey started ${flagsProvider.getOrElse("")}")
        Thread.sleep(10000)
        println("Land survey at 90%.")
        Thread.sleep(1000)
        println("Land survey complete")
    }
}

val siteClearance by tasks.registering(FlaggedTask::class) { //Takes 11  seconds
    doLast {
        println("Site clearance started ${flagsProvider.getOrElse("")}")
        Thread.sleep(10000)
        println("Site clearance at 90%.")
        Thread.sleep(1000)
        println("Site complete.")
    }
}


