import groovy.transform.Field
import groovy.json.JsonSlurper

@Field final String VALIDATE_SCORECARD_URL = "http://localhost:3000/api/1.0/validateScorecard"


def validate(String bitbucketURL) {
    def response = httpRequest httpMode: 'POST', url: "${VALIDATE_SCORECARD_URL}?bitbucketURL=${bitbucketURL}", timeout: 30, validResponseCodes: '100:500'

    if(response.status == 200) {
        def result = new JsonSlurper().parseText(response.content)
        def status = result['status']
        if(status != 'Completed') {
            return [
                error: true, 
                errorMessage: "You need to complete the scorecard for the git repo URL first."
            ]
        } else {
            return [
                error: false, 
                errorMessage: ""
            ]
        }
    } else if(response.status == 404) {
        return [
            error: true,
            errorMessage: "You need to create a scorecard for the git repo URL on the assessement website first."
        ]
    } else {
        def result = new JsonSlurper().parseText(response.content)
        return [
            error: true,
            errorMessage: result['message']
        ]
    }
}

return this

