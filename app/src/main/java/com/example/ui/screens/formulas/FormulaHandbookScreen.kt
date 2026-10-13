package com.example.ui.screens.formulas

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FormulaEntity
import com.example.ui.components.JeeTopHeader
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.JeeViewModel

data class StandardConstant(
    val symbol: String,
    val name: String,
    val value: String
)

@Composable
fun FormulaHandbookScreen(
    viewModel: JeeViewModel,
    onNavigateToFocus: () -> Unit
) {
    val allFormulas by viewModel.formulas.collectAsState()
    val subjectFilter by viewModel.formulaSubjectFilter.collectAsState()
    val searchQuery by viewModel.formulaSearchQuery.collectAsState()
    val profile by viewModel.profile.collectAsState()

    var showOnlyBookmarked by remember { mutableStateOf(false) }
    var showConstantsCard by remember { mutableStateOf(true) }

    val filteredFormulas = remember(allFormulas, subjectFilter, searchQuery, showOnlyBookmarked) {
        allFormulas.filter { f ->
            val matchSubject = subjectFilter == "ALL" || f.subject.equals(subjectFilter, ignoreCase = true)
            val matchSearch = searchQuery.isBlank() ||
                    f.title.contains(searchQuery, ignoreCase = true) ||
                    f.chapter.contains(searchQuery, ignoreCase = true) ||
                    f.formulaText.contains(searchQuery, ignoreCase = true) ||
                    f.description.contains(searchQuery, ignoreCase = true)
            val matchBookmark = !showOnlyBookmarked || f.isBookmarked

            matchSubject && matchSearch && matchBookmark
        }
    }

    val standardConstants = remember {
        listOf(
            StandardConstant("c", "Speed of Light", "3 × 10⁸ m/s"),
            StandardConstant("h", "Planck's Const", "6.626 × 10⁻³⁴ J·s"),
            StandardConstant("e", "Elem Charge", "1.602 × 10⁻¹⁹ C"),
            StandardConstant("k_B", "Boltzmann", "1.38 × 10⁻²³ J/K"),
            StandardConstant("R", "Gas Const", "8.314 J/(mol·K) ≈ 25/3"),
            StandardConstant("ε₀", "Permittivity", "8.854 × 10⁻¹² F/m"),
            StandardConstant("1/4πε₀", "Coulomb Const", "9 × 10⁹ N·m²/C²"),
            StandardConstant("μ₀", "Permeability", "4π × 10⁻⁷ T·m/A"),
            StandardConstant("g", "Grav Accel", "9.8 m/s² ≈ π²")
        )
    }

    Scaffold(
        topBar = {
            JeeTopHeader(
                title = "Formula Handbook",
                subtitle = "PCM Formulae & Cheat Sheets",
                streakDays = profile?.currentStreak ?: 12,
                targetYear = profile?.targetYear ?: 2026,
                onFocusClick = onNavigateToFocus
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Search Input
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setFormulaSearch(it) },
                    modifier = Modifier.fillMaxWidth().testTag("search_formula_input"),
                    placeholder = { Text("Search formulae (e.g. Nernst, Leibniz, Moment of Inertia)...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setFormulaSearch("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Subject Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("ALL" to "All", "PHYSICS" to "Physics", "CHEMISTRY" to "Chemistry", "MATHEMATICS" to "Maths").forEach { (key, label) ->
                            FilterChip(
                                selected = subjectFilter == key,
                                onClick = { viewModel.setFormulaSubjectFilter(key) },
                                label = { Text(label, fontSize = 11.sp) },
                                modifier = Modifier.testTag("formula_filter_$key")
                            )
                        }
                    }

                    FilterChip(
                        selected = showOnlyBookmarked,
                        onClick = { showOnlyBookmarked = !showOnlyBookmarked },
                        label = { Text("★ Saved", fontSize = 10.sp) },
                        modifier = Modifier.testTag("formula_saved_toggle")
                    )
                }
            }

            // High Yield Physical Constants Quick Shelf
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Calculate,
                                    contentDescription = null,
                                    tint = MathAmberLight,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Standard Physical Constants",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }

                            TextButton(onClick = { showConstantsCard = !showConstantsCard }) {
                                Text(
                                    text = if (showConstantsCard) "Collapse" else "Expand",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        AnimatedVisibility(visible = showConstantsCard) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(standardConstants) { c ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = Slate800,
                                            modifier = Modifier.widthIn(min = 110.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text(
                                                    text = c.symbol,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 13.sp,
                                                    color = PhysicsCyan
                                                )
                                                Text(
                                                    text = c.name,
                                                    fontSize = 10.sp,
                                                    color = Slate400,
                                                    maxLines = 1
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = c.value,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Slate200
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (filteredFormulas.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(44.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("No formulas found", fontWeight = FontWeight.Bold)
                            Text("Try searching for another chapter or topic.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                items(filteredFormulas, key = { it.id }) { formula ->
                    FormulaCard(
                        formula = formula,
                        onToggleBookmark = {
                            viewModel.toggleFormulaBookmark(formula.id, !formula.isBookmarked)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FormulaCard(
    formula: FormulaEntity,
    onToggleBookmark: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("formula_card_${formula.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SubjectBadge(subjectName = formula.subject)
                    Text(
                        text = formula.chapter,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(32.dp).testTag("bookmark_formula_${formula.id}")
                ) {
                    Icon(
                        imageVector = if (formula.isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (formula.isBookmarked) WarningGold else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formula.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Main Formula Box (Highlight)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Slate950,
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                )
            ) {
                Box(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = formula.formulaText,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PhysicsCyan,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = formula.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            if (formula.variablesDef.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Variables: ${formula.variablesDef}",
                    fontSize = 11.sp,
                    color = Slate400,
                    lineHeight = 16.sp
                )
            }

            if (formula.keyTip.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MathAmber.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = MathAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "JEE Trap / Tip: ${formula.keyTip}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MathAmberLight,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
