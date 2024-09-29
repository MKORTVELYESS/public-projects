
tasks.register("sitePreparation") {//If paralellism work should take ~21 seconds

    dependsOn(landSurvey, siteClearance)
    doLast {
        println("Site preparation in progress: Land surveying and site clearance.")
        // Add any additional logic here, such as simulating site preparation actions.
    }

}
val landSurvey by tasks.registering {//Takes 21 seconds
    doLast {
        println("Land survey started.")
        Thread.sleep(10000)
        println("Land survey at 30%.")
        Thread.sleep(5000)
        println("Land survey at 60%.")
        Thread.sleep(5000)
        println("Land survey at 90%.")
        Thread.sleep(1000)
        println("Land survey complete")
    }
}

val siteClearance by tasks.registering { //Takes 14  seconds
    doLast {
        println("Site clearance started.")
        Thread.sleep(10000)
        println("Site clearance at 30%.")
        Thread.sleep(1500)
        println("Site clearance at 60%.")
        Thread.sleep(1500)
        println("Site clearance at 90%.")
        Thread.sleep(1000)
        println("Site complete.")
    }
}


