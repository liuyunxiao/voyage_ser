package com.common.tools.clientvo;

class Tool {
	def javaVODir = ""
	def flexVODir = ""
	// def flexVODir = "D://_vo//flash//"
	// def javaVODir = "D://_vo//java//"

	def start() {
		// 删除flex vo
		deleteFlexFiles(new File(flexVODir))

		//写文件
		new File(javaVODir).eachFile { File jfile ->
			writeFlexFile(jfile)
		}
	}

	def getJavaFields(File file) {
		if(!file.canRead()) return
			def list = []
		BufferedReader reader = file.newReader "utf-8"
		reader.eachLine { line ->
			if(line.contains("static") || !line.contains(";")) return
				def index = line.indexOf(";");
			def newLine = line.substring(0,index);
			def matcher = newLine =~ /private( +)(\D*)( +)(\w+)/
			if(matcher.size()) {
				def obj = [
					(matcher[0][2]) ,
					(matcher[0][4])
				]
				list.push obj
			}
		}
		return list
	}

	def log(str) {
		println str
	}

	// 删除flex vo文件
	def deleteFlexFiles(File dir) {
		log "start delete flex file ..."
		dir.eachFile { File file ->
			def name = file.getName()
			if(!file.isDirectory() && !name.contains("FVO") && name.contains("VO")) {
				file.delete()
			}
		}
	}

	def writeFlexFile(File jfile) {
		if(!jfile.getName().contains("java") || jfile.isDirectory()) return
			def javaName = jfile.getName().replace ".java", ""

		def path = flexVODir + javaName + ".as"
		println "--->" + path
		def writer = new File(path).newWriter "utf-8"

		def contentlist = [];
		def tag = false;
		def list = getJavaFields(jfile)
		list.each {
			def key = convertToFlexType(it[0])
			def value = it[1]
			contentlist.push("		public var ${value}:${key};")
			if(value.contains("List")) tag = true;
		}

		writer.writeLine("package com.voyage.data.vo")
		writer.writeLine("{")

		if(tag) {
			writer.writeLine('	import mx.collections.ArrayCollection;')
		}

		writer.writeLine('	[Bindable]')
		writer.writeLine('	[RemoteClass(alias="com.voyage.data.vo.' + javaName + '")]')
		writer.writeLine('	public class ' + javaName)
		writer.writeLine('	{')
		
		contentlist.each { writer.writeLine it }

		writer.writeLine("	}")
		writer.write("}");
		writer.close();
	}

	def convertToFlexType(javaType) {
		if(javaType.equals("Integer")) {
			return "int"
		}
		else if(javaType.contains("List")) {
			return "ArrayCollection"
		}
		else if(javaType.contains("Long") || javaType.contains("Double") || javaType.contains("double") || javaType.contains("float")) {
			return "Number"
		}
		return javaType
	}

	static main(args) {
		Tool tool = new Tool()
		tool.start()
	}
}
