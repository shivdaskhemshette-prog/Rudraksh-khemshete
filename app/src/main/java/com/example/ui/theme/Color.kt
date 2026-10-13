package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Brand Colors - Deep Focus & High-Octane Engineering
val Slate950 = Color(0xFF070B14)
val Slate900 = Color(0xFF0C1322)
val Slate850 = Color(0xFF111A2E)
val Slate800 = Color(0xFF1A2640)
val Slate700 = Color(0xFF2E3F61)
val Slate600 = Color(0xFF475E88)
val Slate400 = Color(0xFF94A3B8)
val Slate200 = Color(0xFFE2E8F0)
val Slate50 = Color(0xFFF8FAFC)

// Electric JEE Subject Accents (Vibrant Neon Glow)
val PhysicsCyan = Color(0xFF00E5FF)
val PhysicsCyanGlow = Color(0xFF38BDF8)
val PhysicsCyanDark = Color(0xFF0284C7)

val ChemEmerald = Color(0xFF10B981)
val ChemEmeraldLight = Color(0xFF34D399)
val ChemMint = Color(0xFF6EE7B7)

val MathAmber = Color(0xFFF59E0B)
val MathAmberLight = Color(0xFFFBBF24)
val MathSunset = Color(0xFFFB923C)

// Functional Accent Tokens
val AccentIndigo = Color(0xFF6366F1)
val AccentPurple = Color(0xFF8B5CF6)
val AccentViolet = Color(0xFFA855F7)
val SuccessGreen = Color(0xFF22C55E)
val ErrorRose = Color(0xFFF43F5E)
val WarningGold = Color(0xFFEAB308)

// Dynamic Gradient Brushes for Glassmorphic Surfaces
val PhysicsGradient = Brush.horizontalGradient(
    listOf(Color(0xFF00E5FF), Color(0xFF0284C7))
)

val ChemGradient = Brush.horizontalGradient(
    listOf(Color(0xFF34D399), Color(0xFF059669))
)

val MathGradient = Brush.horizontalGradient(
    listOf(Color(0xFFFBBF24), Color(0xFFEA580C))
)

val PremiumHeroGradient = Brush.verticalGradient(
    listOf(Color(0xFF1E1B4B), Color(0xFF111A2E), Color(0xFF070B14))
)

val CyberGlowBorder = Brush.linearGradient(
    listOf(PhysicsCyan.copy(alpha = 0.6f), AccentIndigo.copy(alpha = 0.4f), Color.Transparent)
)

val GoldGlowBorder = Brush.linearGradient(
    listOf(WarningGold.copy(alpha = 0.7f), MathSunset.copy(alpha = 0.4f), Color.Transparent)
)
