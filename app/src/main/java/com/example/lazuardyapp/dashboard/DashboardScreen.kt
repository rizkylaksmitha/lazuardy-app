package com.example.lazuardyapp.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lazuardyapp.ui.screens.BottomNavigationBar
import com.example.lazuardyapp.ui.screens.PrimaryColor
import com.example.lazuardyapp.viewmodel.DashboardViewModel


@Composable
fun DashboardScreen(
    onNavigateToJadwal: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToTutorSelection: (String) -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val userName by viewModel.userName.collectAsState()
    val activePackageData = viewModel.activePackageData
    val isActive = viewModel.isUserActive
    val isLoading = viewModel.isLoading
    val onNavigateToHome: () -> Unit = {}

    var isSubjectGridExpanded by remember { mutableStateOf(false) }

    val toggleExpand: () -> Unit = {
        isSubjectGridExpanded = !isSubjectGridExpanded
    }

    Scaffold(
        containerColor = PrimaryColor,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 0,
                onNavigateToHome = onNavigateToHome,
                onNavigateToJadwal = onNavigateToJadwal,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                ProfileHeaderBar(
                    name = userName,
                    isActive = isActive,
                    onNavigateToProfile = onNavigateToProfile,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                BimbelCard()

                Spacer(modifier = Modifier.height(24.dp))

                SearchPackageCard(
                    isExpanded = isSubjectGridExpanded,
                    onSubjectClick = { subjectItem ->
                        onNavigateToTutorSelection(subjectItem.title)
                    },
                    onExpandClick = toggleExpand
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (isLoading) {
                    Text(
                        text = "Memuat progres...",
                        modifier = Modifier.padding(16.dp)
                    )
                } else if (activePackageData.isNotEmpty()) {

                    ProgressSummaryContainer(
                        activePackages = activePackageData,
                        onViewDetail = onNavigateToJadwal
                    )

                } else {
                    ProgressPlaceholder(onNavigateToSelection = {
                    })
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}