package nhs.mirth.sample

import spock.lang.Specification
import org.mozilla.javascript.*

class SampleSpec extends Specification {

	Context cx
	Scriptable scope

	def setup(){
		 cx = Context.enter()

		// Set version to JavaScript1.2 so that we get object-literal style
		// printing instead of "[object Object]"
		cx.setLanguageVersion(Context.VERSION_1_2)

		// Initialize the standard objects (Object, Function, etc.)
		// This must be done before scripts can be executed.
		scope = cx.initStandardObjects()

		String javaScriptFile = new File("src/main/js/sampleScript.js").text
		cx.evaluateString(scope, javaScriptFile, "fileUnderTest", 1, null)
	}

	def cleanup(){
		Context.exit();
	}


	def "Simple object creation"(){

		when: "the JS is evaluated"
		cx.evaluateString(scope, javaScriptExpression, "MySource", 1, null)
		Scriptable obj = (Scriptable) scope.get("obj", scope)

		then: "the objects are populated correctly."
		obj.q == expectedName
		obj.a == 1
		obj.b == [123,'456']

		where:
		[javaScriptExpression, expectedName] << [
				["var result = sayHello('myName');", 'myName'],
				["var result = sayHello(123);", 123],
		]
	}

}