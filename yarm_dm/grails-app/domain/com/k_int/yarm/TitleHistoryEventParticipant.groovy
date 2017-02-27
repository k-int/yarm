package com.k_int.yarm

import javax.persistence.Transient

class TitleHistoryEventParticipant {

  def TitleHistoryEvent event
  def GlobalResource participant
  def String participantRole // in/out

  static belongsTo = [ event:TitleHistoryEvent ]
}
