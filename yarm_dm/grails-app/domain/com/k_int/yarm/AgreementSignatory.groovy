package com.k_int.yarm

public class AgreementSignatory {
  
  Tenant signatory
  Agreement agreement
  RefdataValue status
  RefdataValue activeYN

  static mapping = {
    id column:'as_id'
    version column:'as_version'
    signatory column:'as_signatory_fk'
    agreement column:'as_agreement_fk'
    status column:'as_status_rdv_fk'
    activeYN column:'as_active_rdv_fk'
  }

  static constraints = {
     signatory (nullable:false, blank:false)
     agreement (nullable:false, blank:false)
        status (nullable:false, blank:false)
      activeYN (nullable:true, blank:false)
  }

}
