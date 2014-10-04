package com.rosten.app.share

import com.rosten.app.system.SystemCode

class ShareService {
	
	def addFlowLog ={ belongToId,flowCode,currentUser,content ->
		//添加日志
		def _log = new FlowLog()
		_log.user = currentUser
		_log.belongToId = belongToId
		_log.belongToObject = flowCode
		_log.content = content
		_log.company = currentUser.company
		_log.save(flush:true)
	}
	def getSystemCodeItems={company,code ->
		def systemCode = SystemCode.findByCompanyAndCode(company,code)
		if(systemCode){
			return systemCode.items
		}else{
			return []
		}
	}
	
  def getAllDepartByChild ={departList,depart->
		departList << depart
		if(depart.children){
			depart.children.each{
				return getAllDepartByChild(departList,it)
			}
		}else{
			return departList
		}
	}
  
  
}
