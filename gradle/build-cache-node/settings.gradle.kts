rootProject.name = "build-cache-node"
buildCache {
    local {
        isEnabled = true
    }
    remote<HttpBuildCache> {
        url = uri("http://localhost:5071/cache/")
        isPush = true
        isAllowUntrustedServer = true
        isAllowInsecureProtocol = true
    }
}


