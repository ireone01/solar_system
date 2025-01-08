package com.wavez.trackerwater.evenbus

import com.wavez.trackerwater.data.model.HistoryModel

data class RecordAddedEvent(val newRecord: HistoryModel)
