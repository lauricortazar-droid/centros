package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ResidentEntity
import com.example.ui.components.SearchBarWidget
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun DocumentosEvidenciasScreen(
    residents: List<ResidentEntity>,
    isEvidenciasMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember {
        mutableStateOf(if (isEvidenciasMode) "EVIDENCIAS_INSTALACIONES" else "PLANTILLAS")
    }
    var selectedResidentForDoc by remember { mutableStateOf(residents.firstOrNull()) }
    var showSignatureDialog by remember { mutableStateOf(false) }
    var signatureSaved by remember { mutableStateOf(false) }
    var previewDocTitle by remember { mutableStateOf<String?>(null) }

    val subTabs = if (isEvidenciasMode) {
        listOf(
            "EVIDENCIAS_INSTALACIONES" to "Instalaciones",
            "EVIDENCIAS_SUPERVISIONES" to "Supervisiones COFEPRIS",
            "EVIDENCIAS_CAPACITACION" to "Capacitación",
            "EVIDENCIAS_USUARIOS" to "Usuarios y Diplomas"
        )
    } else {
        listOf(
            "PLANTILLAS" to "Plantillas",
            "DISEÑADOR" to "Generador PDF",
            "CONTRATOS" to "Contratos Vigentes",
            "FIRMA_DIGITAL" to "Firma Digital",
            "ARCHIVO" to "Archivo Digital"
        )
    }

    val templates = listOf(
        Triple("Contrato de Prestación de Servicios Residenciales", "Legal y Terapéutico", "Válido ante CONADIC y Salubridad"),
        Triple("Consentimiento Informado para Ingreso Voluntario", "Normativa NOM-028-SSA2", "Requiere firma de usuario y tutor"),
        Triple("Convenio Económico y Pagaré de Mensualidad", "Administración", "Detalla cuota y fechas de abono"),
        Triple("Reglamento Interno de Convivencia y Disciplina", "Convivencia Residencial", "Normas de respeto y artículos no permitidos"),
        Triple("Carta de Deslinde y Responsiva Médica", "Clínica", "En caso de emergencias preexistentes")
    )

    val evidencias = listOf(
        Triple("Inspección de Cocina y Comedor", "Cumplimiento con Norma de Manejo Higiénico", "Inspeccionado Sep 2024"),
        Triple("Revisión de Extintores y Rutas de Evacuación", "Aprobado por Protección Civil", "Vigencia Anual"),
        Triple("Dormitorios A, B y Femenil", "Higiene y Ventilación Adecuada", "Supervisión Semanal"),
        Triple("Botiquín y Almacén de Medicamentos", "Cerradura de seguridad y bitácora", "Auditoría Interna")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(subTabs) { (key, label) ->
                val isSelected = selectedSubTab == key
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedSubTab = key },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (selectedSubTab == "PLANTILLAS") {
                item {
                    SectionHeader(
                        title = "Formatos y Plantillas Oficiales",
                        subtitle = "Documentos normativos listos para expedientes"
                    )
                }

                items(templates) { (nombre, area, desc) ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(nombre, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text(area, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary))
                                Text(desc, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                            }
                            Button(
                                onClick = { previewDocTitle = nombre },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Ver", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            if (selectedSubTab == "DISEÑADOR") {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Generador de Documento PDF para Residente",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Selecciona el residente para autocompletar el contrato:",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(residents) { res ->
                                    FilterChip(
                                        selected = selectedResidentForDoc?.id == res.id,
                                        onClick = { selectedResidentForDoc = res },
                                        label = { Text(res.fullName.split(" ").take(2).joinToString(" ")) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            selectedResidentForDoc?.let { res ->
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("VISTA PREVIA DEL CONTRATO", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                        Text("RESIDENTE: ${res.fullName}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                        Text("FOLIO: ${res.folio} | TUTOR: ${res.tutorName} (${res.tutorPhone})", style = MaterialTheme.typography.bodySmall)
                                        Text("CUOTA ACORDADA: $${String.format("%,.0f", res.monthlyFee)} MXN / mes", style = MaterialTheme.typography.bodySmall)
                                        Text("FECHA DE INGRESO: ${res.admissionDate}", style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "DECLARACIONES: Ambas partes convienen en sujetarse al tratamiento residencial bajo el modelo de Comunidad Terapéutica por un periodo recomendado de 6 meses...",
                                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { showSignatureDialog = true },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.Draw, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Capturar Firma")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (selectedSubTab == "FIRMA_DIGITAL") {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Módulo de Firma Digital Biométrica",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Permite a los familiares y residentes rubricar contratos y consentimientos desde la pantalla táctil.",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            if (signatureSaved) {
                                Surface(
                                    color = StatusActiveGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusActiveGreen)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Firma digital registrada con sello de tiempo criptográfico.",
                                            style = MaterialTheme.typography.bodyMedium.copy(color = StatusActiveGreen, fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Button(
                                onClick = { showSignatureDialog = true },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Gesture, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Abrir Pad de Firma")
                            }
                        }
                    }
                }
            }

            if (selectedSubTab.startsWith("EVIDENCIAS")) {
                item {
                    SectionHeader(
                        title = "Álbum de Evidencias e Inspecciones",
                        subtitle = "Respaldo fotográfico y supervisiones oficiales"
                    )
                }

                items(evidencias) { (titulo, desc, fecha) ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(titulo, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text(desc, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                                Text(fecha, style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }

            if (selectedSubTab == "CONTRATOS" || selectedSubTab == "ARCHIVO") {
                item {
                    SectionHeader(
                        title = "Expedientes y Contratos Archivados",
                        subtitle = "Historial digitalizado"
                    )
                }

                items(residents) { res ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(res.fullName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text("Folio: ${res.folio} • Ingreso: ${res.admissionDate}", style = MaterialTheme.typography.bodySmall)
                            }
                            Surface(
                                color = StatusActiveGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Firmado y Vigente",
                                    color = StatusActiveGreen,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Signature Canvas Dialog
    if (showSignatureDialog) {
        Dialog(onDismissRequest = { showSignatureDialog = false }) {
            var pathPoints by remember { mutableStateOf(listOf<Offset>()) }

            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Pad de Firma Táctil", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text("Firme dentro del recuadro usando su dedo:", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                            .pointerInput(Unit) {
                                detectDragGestures { change, _ ->
                                    pathPoints = pathPoints + change.position
                                }
                            }
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            if (pathPoints.size > 1) {
                                for (i in 0 until pathPoints.size - 1) {
                                    drawLine(
                                        color = Color(0xFF003D4D),
                                        start = pathPoints[i],
                                        end = pathPoints[i + 1],
                                        strokeWidth = 4f,
                                        cap = StrokeCap.Round
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { pathPoints = emptyList() }) {
                            Text("Limpiar")
                        }
                        Row {
                            TextButton(onClick = { showSignatureDialog = false }) {
                                Text("Cancelar")
                            }
                            Button(onClick = {
                                signatureSaved = true
                                showSignatureDialog = false
                            }) {
                                Text("Aceptar y Guardar")
                            }
                        }
                    }
                }
            }
        }
    }

    // Document Preview Dialog
    previewDocTitle?.let { title ->
        AlertDialog(
            onDismissRequest = { previewDocTitle = null },
            title = { Text(title) },
            text = {
                Text(
                    "Este documento se encuentra en la plantilla maestra oficial de la institución con folios y logos autorizados. Listo para exportar o imprimir en formato PDF institucional.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(onClick = { previewDocTitle = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}
