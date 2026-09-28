package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.auth.AuthUser
import com.example.data.auth.UserRole
import com.example.ui.navigation.MainSection
import com.example.ui.theme.StatusActiveGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendaTopAppBar(
    currentSection: MainSection,
    onMenuClick: () -> Unit,
    pendingAlertsCount: Int = 0,
    currentUser: AuthUser? = null,
    onAuthClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = currentSection.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                if (currentUser != null) {
                    Text(
                        text = "${currentUser.role.label} • ${currentUser.displayName}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.testTag("nav_drawer_open_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Abrir menú de navegación"
                )
            }
        },
        actions = {
            // Auth / Profile Button
            Surface(
                onClick = onAuthClick,
                shape = RoundedCornerShape(20.dp),
                color = if (currentUser != null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .testTag("appbar_auth_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = when (currentUser?.role) {
                            UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                            UserRole.CLINICO -> Icons.Default.MedicalServices
                            UserRole.OPERATIVO -> Icons.Default.Security
                            else -> Icons.Default.Lock
                        },
                        contentDescription = "Autenticación",
                        tint = if (currentUser != null) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentUser?.role?.label?.take(8) ?: "Acceso",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (currentUser != null) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            IconButton(
                onClick = { /* notification sheet or alerts */ },
                modifier = Modifier.testTag("notifications_button")
            ) {
                BadgedBox(
                    badge = {
                        if (pendingAlertsCount > 0) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ) {
                                Text("$pendingAlertsCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Alertas del Centro"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
}

@Composable
fun SendaBottomBar(
    currentSection: MainSection,
    onSectionSelected: (MainSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomNavSections = listOf(
        MainSection.INICIO,
        MainSection.USUARIOS,
        MainSection.CLINICA,
        MainSection.FINANZAS,
        MainSection.AGENDA
    )

    NavigationBar(
        modifier = modifier.testTag("senda_bottom_navigation"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = NavigationBarDefaults.Elevation
    ) {
        bottomNavSections.forEach { section ->
            val isSelected = currentSection == section
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSectionSelected(section) },
                icon = {
                    Icon(
                        imageVector = section.icon,
                        contentDescription = section.title
                    )
                },
                label = {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
