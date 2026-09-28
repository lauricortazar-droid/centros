package com.example.data.repository

import com.example.data.local.ExpedienteDao
import com.example.data.local.SendaDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class SendaRepository(
    private val dao: SendaDao,
    private val expedienteDao: ExpedienteDao
) {

    // Residents
    val allResidents: Flow<List<ResidentEntity>> = dao.getAllResidents()
    fun getResidentsByStatus(status: String): Flow<List<ResidentEntity>> = dao.getResidentsByStatus(status)
    suspend fun getResidentById(id: Int): ResidentEntity? = dao.getResidentById(id)
    suspend fun insertResident(resident: ResidentEntity): Long = dao.insertResident(resident)
    suspend fun updateResident(resident: ResidentEntity) = dao.updateResident(resident)
    suspend fun deleteResident(resident: ResidentEntity) = dao.deleteResident(resident)

    // Expedientes Clínicos
    val allExpedientes: Flow<List<ExpedienteEntity>> = expedienteDao.getAllExpedientes()
    fun getExpedienteByResidentId(residentId: Int): Flow<ExpedienteEntity?> =
        expedienteDao.getExpedienteByResidentId(residentId)
    suspend fun getExpedienteById(id: Int): ExpedienteEntity? = expedienteDao.getExpedienteById(id)
    fun searchExpedientes(query: String): Flow<List<ExpedienteEntity>> =
        expedienteDao.searchExpedientes(query)
    suspend fun insertExpediente(expediente: ExpedienteEntity): Long =
        expedienteDao.insertExpediente(expediente)
    suspend fun updateExpediente(expediente: ExpedienteEntity) =
        expedienteDao.updateExpediente(expediente)
    suspend fun deleteExpediente(expediente: ExpedienteEntity) =
        expedienteDao.deleteExpediente(expediente)

    // Clinical Records
    val allClinicalRecords: Flow<List<ClinicalRecordEntity>> = dao.getAllClinicalRecords()
    fun getClinicalRecordsForResident(residentId: Int): Flow<List<ClinicalRecordEntity>> =
        dao.getClinicalRecordsForResident(residentId)
    suspend fun insertClinicalRecord(record: ClinicalRecordEntity) = dao.insertClinicalRecord(record)
    suspend fun deleteClinicalRecord(record: ClinicalRecordEntity) = dao.deleteClinicalRecord(record)

    // Medications
    val allMedications: Flow<List<MedicationEntity>> = dao.getAllMedications()
    fun getMedicationsForResident(residentId: Int): Flow<List<MedicationEntity>> =
        dao.getMedicationsForResident(residentId)
    suspend fun insertMedication(medication: MedicationEntity) = dao.insertMedication(medication)
    suspend fun updateMedication(medication: MedicationEntity) = dao.updateMedication(medication)
    suspend fun deleteMedication(medication: MedicationEntity) = dao.deleteMedication(medication)

    // Finance
    val allTransactions: Flow<List<FinanceTransactionEntity>> = dao.getAllFinanceTransactions()
    suspend fun insertFinanceTransaction(transaction: FinanceTransactionEntity) =
        dao.insertFinanceTransaction(transaction)
    suspend fun deleteFinanceTransaction(transaction: FinanceTransactionEntity) =
        dao.deleteFinanceTransaction(transaction)

    // Agenda
    val allAgendaEvents: Flow<List<AgendaEventEntity>> = dao.getAllAgendaEvents()
    suspend fun insertAgendaEvent(event: AgendaEventEntity) = dao.insertAgendaEvent(event)
    suspend fun deleteAgendaEvent(event: AgendaEventEntity) = dao.deleteAgendaEvent(event)

    // Staff
    val allStaff: Flow<List<StaffMemberEntity>> = dao.getAllStaff()
    suspend fun insertStaff(staff: StaffMemberEntity) = dao.insertStaff(staff)
    suspend fun deleteStaff(staff: StaffMemberEntity) = dao.deleteStaff(staff)

    // Operation
    val allOperationLogs: Flow<List<OperationLogEntity>> = dao.getAllOperationLogs()
    suspend fun insertOperationLog(log: OperationLogEntity) = dao.insertOperationLog(log)
    suspend fun updateOperationLog(log: OperationLogEntity) = dao.updateOperationLog(log)
    suspend fun deleteOperationLog(log: OperationLogEntity) = dao.deleteOperationLog(log)
}
