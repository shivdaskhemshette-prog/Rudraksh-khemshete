package com.example.data

import com.example.data.model.*

object DefaultJeeData {

    fun getInitialProfile(): StudentProfileEntity {
        return StudentProfileEntity(
            id = 1,
            name = "JEE Warrior",
            targetYear = 2026,
            targetExam = "JEE Main & Advanced",
            targetRank = 1500,
            dailyGoalHours = 8,
            currentStreak = 12,
            totalFocusMinutes = 1840
        )
    }

    fun getInitialSchedule(): List<StudySlotEntity> {
        val days = (1..7) // Monday to Sunday
        val list = mutableListOf<StudySlotEntity>()
        for (day in days) {
            when (day) {
                1 -> { // Monday: Physics Heavy + Maths
                    list.add(StudySlotEntity(dayOfWeek = 1, timeSlot = "06:00 AM - 08:30 AM", subject = Subject.PHYSICS.name, topic = "Rotational Motion - Rolling Dynamics & Angular Momentum", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 1, timeSlot = "09:30 AM - 12:00 PM", subject = Subject.MATHEMATICS.name, topic = "Definite Integration - King's Rule & Periodic Functions", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 1, timeSlot = "02:30 PM - 05:00 PM", subject = Subject.CHEMISTRY.name, topic = "Chemical Kinetics - Integrated Rate Laws & Arrhenius", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 1, timeSlot = "08:00 PM - 10:00 PM", subject = Subject.REVISION.name, topic = "Daily PYQ Drill: 30 Qs Mixed (10P + 10C + 10M)", durationMinutes = 120))
                }
                2 -> { // Tuesday: Chem Heavy + Physics
                    list.add(StudySlotEntity(dayOfWeek = 2, timeSlot = "06:00 AM - 08:30 AM", subject = Subject.CHEMISTRY.name, topic = "Coordination Compounds - CFT & IUPAC Nomenclature", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 2, timeSlot = "09:30 AM - 12:00 PM", subject = Subject.PHYSICS.name, topic = "Electrostatics - Gauss Law & Variable Density Spheres", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 2, timeSlot = "02:30 PM - 05:00 PM", subject = Subject.MATHEMATICS.name, topic = "Application of Derivatives - Maxima/Minima & Tangents", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 2, timeSlot = "08:00 PM - 10:00 PM", subject = Subject.REVISION.name, topic = "Formula Flashcards & Error Log Analysis", durationMinutes = 120))
                }
                3 -> { // Wednesday: Math Heavy + Chem
                    list.add(StudySlotEntity(dayOfWeek = 3, timeSlot = "06:00 AM - 08:30 AM", subject = Subject.MATHEMATICS.name, topic = "Coordinate Geometry - Parabola & Focal Chords", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 3, timeSlot = "09:30 AM - 12:00 PM", subject = Subject.CHEMISTRY.name, topic = "Aldehydes & Ketones - Aldol, Cannizzaro & Grignard", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 3, timeSlot = "02:30 PM - 05:00 PM", subject = Subject.PHYSICS.name, topic = "Current Electricity - Kirchhoff's Laws & Potentiometer", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 3, timeSlot = "08:00 PM - 10:00 PM", subject = Subject.REVISION.name, topic = "Backlog Clearing: Indefinite Integrals Standard Forms", durationMinutes = 120))
                }
                4 -> { // Thursday: Full Synergy
                    list.add(StudySlotEntity(dayOfWeek = 4, timeSlot = "06:00 AM - 08:30 AM", subject = Subject.PHYSICS.name, topic = "Thermodynamics & KTG - PV Work & Polytropic Processes", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 4, timeSlot = "09:30 AM - 12:00 PM", subject = Subject.MATHEMATICS.name, topic = "Complex Numbers - Euler Form & Geometry of Argand Plane", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 4, timeSlot = "02:30 PM - 05:00 PM", subject = Subject.CHEMISTRY.name, topic = "Ionic Equilibrium - Buffer Solutions & Solubility Product", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 4, timeSlot = "08:00 PM - 10:00 PM", subject = Subject.REVISION.name, topic = "Speed Solving Drill: Numerical Type Problem Mastery", durationMinutes = 120))
                }
                5 -> { // Friday: High Yield
                    list.add(StudySlotEntity(dayOfWeek = 5, timeSlot = "06:00 AM - 08:30 AM", subject = Subject.CHEMISTRY.name, topic = "d & f Block Elements - Oxidation States & Colored Ions", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 5, timeSlot = "09:30 AM - 12:00 PM", subject = Subject.PHYSICS.name, topic = "Electromagnetic Induction - Lenz Law & Mutual Inductance", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 5, timeSlot = "02:30 PM - 05:00 PM", subject = Subject.MATHEMATICS.name, topic = "Vectors & 3D - Shortest Distance Between Skew Lines", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 5, timeSlot = "08:00 PM - 10:00 PM", subject = Subject.REVISION.name, topic = "Pre-Mock Mental Priming & Strategy Review", durationMinutes = 120))
                }
                6 -> { // Saturday: Mock Test Day!
                    list.add(StudySlotEntity(dayOfWeek = 6, timeSlot = "09:00 AM - 12:00 PM", subject = Subject.MOCK_TEST.name, topic = "JEE Main Full Syllabus Mock Test (All 75 Qs)", durationMinutes = 180))
                    list.add(StudySlotEntity(dayOfWeek = 6, timeSlot = "02:00 PM - 05:00 PM", subject = Subject.REVISION.name, topic = "Mock In-Depth Post-Mortem & Error Log Recording", durationMinutes = 180))
                    list.add(StudySlotEntity(dayOfWeek = 6, timeSlot = "07:30 PM - 09:30 PM", subject = Subject.PHYSICS.name, topic = "Modern Physics - Photoelectric Effect & de Broglie", durationMinutes = 120))
                }
                7 -> { // Sunday: Consolidation & Backlog buffer
                    list.add(StudySlotEntity(dayOfWeek = 7, timeSlot = "07:00 AM - 10:00 AM", subject = Subject.REVISION.name, topic = "Weekly Macro Revision - All 3 Subjects Important Concepts", durationMinutes = 180))
                    list.add(StudySlotEntity(dayOfWeek = 7, timeSlot = "11:00 AM - 01:30 PM", subject = Subject.MATHEMATICS.name, topic = "Matrices & Determinants - Cayley-Hamilton & System of Eqns", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 7, timeSlot = "03:30 PM - 06:00 PM", subject = Subject.CHEMISTRY.name, topic = "Thermodynamics - Gibbs Free Energy & Spontaneity", durationMinutes = 150))
                    list.add(StudySlotEntity(dayOfWeek = 7, timeSlot = "08:00 PM - 09:30 PM", subject = Subject.REVISION.name, topic = "Next Week Schedule Planning & Goal Setting", durationMinutes = 90))
                }
            }
        }
        return list
    }

    fun getInitialQuestions(): List<QuestionEntity> {
        return listOf(
            // PHYSICS
            QuestionEntity(
                subject = "PHYSICS",
                chapter = "Rotational Motion",
                questionType = QuestionType.MCQ.name,
                difficulty = Difficulty.MEDIUM.name,
                questionText = "A solid sphere of mass M and radius R rolls without slipping down an inclined plane of inclination θ. What is the acceleration of its center of mass?",
                optionA = "g sin(θ)",
                optionB = "(5/7) g sin(θ)",
                optionC = "(2/3) g sin(θ)",
                optionD = "(5/9) g sin(θ)",
                correctAnswer = "B",
                explanation = "For rolling without slipping on an inclined plane:\na = (g sin θ) / (1 + I / (M R²))\nFor a solid sphere, I = (2/5) M R²\nTherefore, 1 + I / (M R²) = 1 + 2/5 = 7/5\nHence, a = (g sin θ) / (7/5) = (5/7) g sin θ.",
                pyqYear = "JEE Main 2023"
            ),
            QuestionEntity(
                subject = "PHYSICS",
                chapter = "Electrostatics",
                questionType = QuestionType.NUMERICAL.name,
                difficulty = Difficulty.TOUGH.name,
                questionText = "A non-conducting sphere of radius R = 10 cm has a uniform volume charge density ρ. If the electric field at a distance of 5 cm from the center is 18 N/C, calculate the magnitude of the electric field (in N/C) at a distance of 20 cm from the center.",
                optionA = "", optionB = "", optionC = "", optionD = "",
                correctAnswer = "9",
                explanation = "Inside the sphere (r₁ = 5 cm < R):\nE₁ = (ρ · r₁) / (3 ε₀) = 18 N/C.\nThus (ρ / 3 ε₀) = 18 / 0.05 = 360 N/(C·m).\nTotal charge Q = ρ · (4/3) π R³.\nOutside the sphere (r₂ = 20 cm > R):\nE₂ = Q / (4 π ε₀ r₂²) = (ρ R³) / (3 ε₀ r₂²)\nE₂ = (ρ / 3 ε₀) · (R³ / r₂²) = 360 · (0.1³ / 0.2²) = 360 · (0.001 / 0.04) = 360 · 0.025 = 9 N/C.",
                pyqYear = "JEE Advanced 2022"
            ),
            QuestionEntity(
                subject = "PHYSICS",
                chapter = "Modern Physics",
                questionType = QuestionType.MCQ.name,
                difficulty = Difficulty.MEDIUM.name,
                questionText = "When light of wavelength λ falls on a metal surface, stopping potential is 3V₀. When wavelength is doubled (2λ), the stopping potential drops to V₀. The threshold wavelength for this metal is:",
                optionA = "3λ",
                optionB = "4λ",
                optionC = "5λ",
                optionD = "6λ",
                correctAnswer = "B",
                explanation = "From photoelectric equation:\ne(3V₀) = hc/λ - φ  --- (1)\ne(V₀) = hc/(2λ) - φ --- (2)\nMultiply (2) by 3:\n3eV₀ = 3hc/(2λ) - 3φ\nEquate with (1):\nhc/λ - φ = 3hc/(2λ) - 3φ\n2φ = 3hc/(2λ) - hc/λ = hc/(2λ)\nφ = hc/(4λ)\nSince work function φ = hc/λ₀, we have λ₀ = 4λ.",
                pyqYear = "JEE Main 2024"
            ),
            QuestionEntity(
                subject = "PHYSICS",
                chapter = "Current Electricity",
                questionType = QuestionType.MCQ.name,
                difficulty = Difficulty.EASY.name,
                questionText = "In a meter bridge experiment, null point is obtained at 40 cm when resistance in left gap is 2 Ω. What is the value of resistance in the right gap?",
                optionA = "2 Ω",
                optionB = "3 Ω",
                optionC = "4 Ω",
                optionD = "1.5 Ω",
                correctAnswer = "B",
                explanation = "By balanced Wheatstone bridge principle:\nR / S = l / (100 - l)\n2 / S = 40 / (100 - 40) = 40 / 60 = 2 / 3\nS = 3 Ω.",
                pyqYear = "JEE Main 2023"
            ),
            QuestionEntity(
                subject = "PHYSICS",
                chapter = "Thermodynamics",
                questionType = QuestionType.NUMERICAL.name,
                difficulty = Difficulty.MEDIUM.name,
                questionText = "A Carnot engine works between temperatures 600 K and 300 K. It absorbs 800 J of heat from the source in each cycle. The mechanical work delivered by the engine per cycle (in Joules) is:",
                optionA = "", optionB = "", optionC = "", optionD = "",
                correctAnswer = "400",
                explanation = "Efficiency η = 1 - (T_sink / T_source) = 1 - (300 / 600) = 0.5 (50%).\nWork Done W = η · Q_in = 0.5 · 800 J = 400 J.",
                pyqYear = "JEE Main 2024"
            ),

            // CHEMISTRY
            QuestionEntity(
                subject = "CHEMISTRY",
                chapter = "Chemical Kinetics",
                questionType = QuestionType.MCQ.name,
                difficulty = Difficulty.MEDIUM.name,
                questionText = "For a first order reaction A → Products, 75% of the reactant decomposes in 32 minutes. The half-life (t_1/2) of the reaction is:",
                optionA = "8 minutes",
                optionB = "16 minutes",
                optionC = "24 minutes",
                optionD = "32 minutes",
                correctAnswer = "B",
                explanation = "For a 1st order reaction, amount remaining after 1 half-life is 50%, after 2 half-lives is 25% (i.e. 75% decomposed).\nTherefore, t_75% = 2 · t_1/2.\n32 min = 2 · t_1/2  ==>  t_1/2 = 16 minutes.",
                pyqYear = "JEE Main 2023"
            ),
            QuestionEntity(
                subject = "CHEMISTRY",
                chapter = "Coordination Compounds",
                questionType = QuestionType.MCQ.name,
                difficulty = Difficulty.TOUGH.name,
                questionText = "Which among the following complexes is diamagnetic and has d²sp³ hybridization?",
                optionA = "[Fe(H₂O)₆]²⁺",
                optionB = "[Fe(CN)₆]⁴⁻",
                optionC = "[Fe(CN)₆]³⁻",
                optionD = "[Ni(CN)₄]²⁻",
                correctAnswer = "B",
                explanation = "In [Fe(CN)₆]⁴⁻, Fe is in +2 oxidation state (3d⁶). CN⁻ is a strong field ligand causing forced pairing of electrons: t₂g⁶ eg⁰.\nAll electrons are paired => Diamagnetic.\nThe inner 3d orbitals (two empty) combine with 4s and 4p orbitals => d²sp³ (octahedral).",
                pyqYear = "JEE Advanced 2021"
            ),
            QuestionEntity(
                subject = "CHEMISTRY",
                chapter = "Organic Chemistry",
                questionType = QuestionType.MCQ.name,
                difficulty = Difficulty.MEDIUM.name,
                questionText = "Which of the following compounds does NOT undergo Aldol condensation in the presence of dilute NaOH?",
                optionA = "CH₃-CHO",
                optionB = "CH₃-CH₂-CHO",
                optionC = "(CH₃)₃C-CHO (Pivalaldehyde)",
                optionD = "CH₃-CO-CH₃",
                correctAnswer = "C",
                explanation = "Aldol condensation requires at least one α-hydrogen atom on the carbonyl carbon.\nIn Pivalaldehyde ((CH₃)₃C-CHO), the α-carbon has 3 methyl groups and ZERO α-hydrogens. Hence, it undergoes Cannizzaro reaction instead.",
                pyqYear = "JEE Main 2024"
            ),
            QuestionEntity(
                subject = "CHEMISTRY",
                chapter = "Ionic Equilibrium",
                questionType = QuestionType.NUMERICAL.name,
                difficulty = Difficulty.MEDIUM.name,
                questionText = "The solubility product (Ksp) of a sparingly soluble salt AB₂ is 4 × 10⁻¹² at 298 K. Calculate its molar solubility S (in units of 10⁻⁴ mol/L).",
                optionA = "", optionB = "", optionC = "", optionD = "",
                correctAnswer = "1",
                explanation = "For AB₂ ⇌ A²⁺ + 2B⁻:\n[A²⁺] = S, [B⁻] = 2S\nKsp = S · (2S)² = 4S³\n4 × 10⁻¹² = 4S³\nS³ = 10⁻¹²\nS = 10⁻⁴ mol/L.\nIn units of 10⁻⁴ mol/L, S = 1.",
                pyqYear = "JEE Main 2022"
            ),

            // MATHEMATICS
            QuestionEntity(
                subject = "MATHEMATICS",
                chapter = "Definite Integrals",
                questionType = QuestionType.MCQ.name,
                difficulty = Difficulty.TOUGH.name,
                questionText = "Evaluate the definite integral: I = ∫[0 to π/2] (sin⁴ x) / (sin⁴ x + cos⁴ x) dx.",
                optionA = "π / 2",
                optionB = "π / 4",
                optionC = "π / 8",
                optionD = "0",
                correctAnswer = "B",
                explanation = "Using King's property: ∫[0 to a] f(x) dx = ∫[0 to a] f(a - x) dx.\nHere a = π/2. Since sin(π/2 - x) = cos x and cos(π/2 - x) = sin x:\nI = ∫[0 to π/2] (cos⁴ x) / (cos⁴ x + sin⁴ x) dx.\nAdding both expressions:\n2I = ∫[0 to π/2] (sin⁴ x + cos⁴ x) / (sin⁴ x + cos⁴ x) dx = ∫[0 to π/2] 1 dx = π/2.\nTherefore, I = π / 4.",
                pyqYear = "JEE Main 2024"
            ),
            QuestionEntity(
                subject = "MATHEMATICS",
                chapter = "Vectors & 3D Geometry",
                questionType = QuestionType.NUMERICAL.name,
                difficulty = Difficulty.MEDIUM.name,
                questionText = "If vectors a = 2i + j - k and b = i - j + λk are mutually perpendicular, find the value of λ.",
                optionA = "", optionB = "", optionC = "", optionD = "",
                correctAnswer = "1",
                explanation = "Two vectors are perpendicular if their dot product is zero: a · b = 0.\n(2)(1) + (1)(-1) + (-1)(λ) = 0\n2 - 1 - λ = 0\n1 - λ = 0  ==>  λ = 1.",
                pyqYear = "JEE Main 2023"
            ),
            QuestionEntity(
                subject = "MATHEMATICS",
                chapter = "Coordinate Geometry",
                questionType = QuestionType.MCQ.name,
                difficulty = Difficulty.MEDIUM.name,
                questionText = "The equation of the tangent to the parabola y² = 8x with slope m = 2 is:",
                optionA = "y = 2x + 1",
                optionB = "y = 2x + 2",
                optionC = "y = 2x - 1",
                optionD = "y = 2x + 4",
                correctAnswer = "A",
                explanation = "For standard parabola y² = 4ax (here 4a = 8 => a = 2),\nthe equation of tangent with slope m is:\ny = mx + a/m\nGiven m = 2 and a = 2:\ny = 2x + 2/2 = 2x + 1.",
                pyqYear = "JEE Main 2023"
            ),
            QuestionEntity(
                subject = "MATHEMATICS",
                chapter = "Complex Numbers",
                questionType = QuestionType.MCQ.name,
                difficulty = Difficulty.TOUGH.name,
                questionText = "If ω is a non-real complex cube root of unity, then the value of (1 - ω + ω²)⁵ + (1 + ω - ω²)⁵ is:",
                optionA = "32",
                optionB = "-32",
                optionC = "64",
                optionD = "-64",
                correctAnswer = "A",
                explanation = "We know 1 + ω + ω² = 0, so 1 + ω² = -ω and 1 + ω = -ω².\nFirst term: (1 + ω² - ω)⁵ = (-ω - ω)⁵ = (-2ω)⁵ = -32 ω⁵ = -32 ω² (since ω³ = 1).\nSecond term: (1 + ω - ω²)⁵ = (-ω² - ω²)⁵ = (-2ω²)⁵ = -32 ω¹⁰ = -32 ω.\nSum = -32 (ω² + ω).\nSince ω² + ω = -1:\nSum = -32 (-1) = 32.",
                pyqYear = "JEE Advanced 2020"
            )
        )
    }

    fun getInitialFormulas(): List<FormulaEntity> {
        return listOf(
            // PHYSICS
            FormulaEntity(
                subject = "PHYSICS",
                chapter = "Rotational Dynamics",
                title = "Moment of Inertia Theorems",
                formulaText = "I = I_cm + M d²  (Parallel Axis)\nI_z = I_x + I_y  (Perpendicular Axis, Planar Laminas)",
                description = "Fundamental theorems to find moment of inertia about any parallel or perpendicular axis.",
                variablesDef = "I_cm: MOI about COM axis, M: Total mass, d: Perpendicular separation between axes.",
                keyTip = "Perpendicular axis theorem ONLY applies to 2D planar planar objects (z-axis perpendicular to sheet)."
            ),
            FormulaEntity(
                subject = "PHYSICS",
                chapter = "Electrostatics",
                title = "Capacitance with Dielectric Slab",
                formulaText = "C = ε₀ A / (d - t + t/K)",
                description = "Capacitance of a parallel-plate capacitor when a dielectric slab of thickness t and dielectric constant K is inserted.",
                variablesDef = "A: Plate area, d: Plate separation, t: Slab thickness (t ≤ d), K: Dielectric constant.",
                keyTip = "If slab is conducting metal (K -> ∞), then C = ε₀ A / (d - t)."
            ),
            FormulaEntity(
                subject = "PHYSICS",
                chapter = "Modern Physics",
                title = "Bohr Energy Levels & Radius",
                formulaText = "r_n = 0.529 · (n² / Z) Å\nE_n = -13.6 · (Z² / n²) eV\nv_n = 2.18 × 10⁶ · (Z / n) m/s",
                description = "Quantized radii, total energies, and orbital speeds in hydrogen-like single-electron species.",
                variablesDef = "n: Principal quantum number, Z: Atomic number.",
                keyTip = "Kinetic energy K = -E_n, Potential energy U = 2 E_n. Energy difference ΔE = hc/λ = R_H Z² (1/n₁² - 1/n₂²)."
            ),
            FormulaEntity(
                subject = "PHYSICS",
                chapter = "Thermodynamics",
                title = "Polytropic Process Specific Heat",
                formulaText = "C = C_v + R / (1 - x)  for  P V^x = constant",
                description = "Molar heat capacity for any general reversible polytropic gas expansion.",
                variablesDef = "C_v: Molar heat capacity at const volume (fR/2), x: Polytropic exponent.",
                keyTip = "For isothermal x = 1 (C -> ∞). For adiabatic x = γ (C = 0). For isobaric x = 0 (C = C_v + R = C_p)."
            ),
            FormulaEntity(
                subject = "PHYSICS",
                chapter = "Wave Optics",
                title = "YDSE Fringe Width & Path Diff",
                formulaText = "β = λ D / d\nΔx = d sin θ ≈ d y / D",
                description = "Young's Double Slit Experiment fringe width and conditions for constructive/destructive interference.",
                variablesDef = "λ: Light wavelength, D: Slit-to-screen distance, d: Distance between slits.",
                keyTip = "In a medium of refractive index μ, fringe width shrinks: β' = β / μ."
            ),

            // CHEMISTRY
            FormulaEntity(
                subject = "CHEMISTRY",
                chapter = "Electrochemistry",
                title = "Nernst Equation",
                formulaText = "E_cell = E°_cell - (0.0591 / n) · log₁₀(Q)  at 298 K",
                description = "Calculates electrode potential under non-standard conditions from standard EMF and reaction quotient.",
                variablesDef = "n: Number of moles of transferred electrons, Q: Reaction quotient = [Products] / [Reactants].",
                keyTip = "At equilibrium, E_cell = 0, which directly gives E°_cell = (0.0591 / n) · log₁₀(K_eq)."
            ),
            FormulaEntity(
                subject = "CHEMISTRY",
                chapter = "Ionic Equilibrium",
                title = "Henderson-Hasselbalch Equation",
                formulaText = "pH = pKa + log₁₀([Conjugate Base] / [Weak Acid])\npOH = pKb + log₁₀([Conjugate Acid] / [Weak Base])",
                description = "Buffer solution pH determination for acidic and basic buffer systems.",
                variablesDef = "pKa = -log(Ka), [Conjugate Base] is usually salt concentration.",
                keyTip = "Maximum buffer capacity occurs when [Salt] = [Acid], giving pH = pKa."
            ),
            FormulaEntity(
                subject = "CHEMISTRY",
                chapter = "Chemical Bonding",
                title = "Bond Order in Molecular Orbital Theory",
                formulaText = "Bond Order = 1/2 · (N_b - N_a)",
                description = "Number of chemical bonds between a pair of atoms based on bonding and anti-bonding MO electrons.",
                variablesDef = "N_b: Electrons in bonding MOs, N_a: Electrons in anti-bonding MOs.",
                keyTip = "If B.O. > 0 molecule is stable. Diamagnetic if all MO electrons paired; paramagnetic if unpaired electrons exist (e.g. O₂ has 2 unpaired electrons in π*2p)."
            ),
            FormulaEntity(
                subject = "CHEMISTRY",
                chapter = "Solutions",
                title = "Colligative Properties Summary",
                formulaText = "ΔT_b = i · K_b · m\nΔT_f = i · K_f · m\nπ = i · C · R · T",
                description = "Boiling point elevation, freezing point depression, and osmotic pressure governed by van 't Hoff factor.",
                variablesDef = "i: van 't Hoff factor = 1 + (n - 1)α for dissociation, m: Molality, C: Molarity.",
                keyTip = "Never forget the van 't Hoff factor i for ionic salts like NaCl (i ≈ 2), BaCl₂ (i ≈ 3)."
            ),

            // MATHEMATICS
            FormulaEntity(
                subject = "MATHEMATICS",
                chapter = "Calculus",
                title = "Leibniz Rule for Differentiation under Integral",
                formulaText = "d/dx [ ∫[u(x) to v(x)] f(t) dt ] = f(v(x)) · v'(x) - f(u(x)) · u'(x)",
                description = "Used repeatedly in JEE Advanced to differentiate variable-limit definite integrals.",
                variablesDef = "v(x): Upper bound, u(x): Lower bound, f(t): Continuous integrand.",
                keyTip = "Frequently tested in 0/0 indeterminate limit questions using L'Hôpital's Rule."
            ),
            FormulaEntity(
                subject = "MATHEMATICS",
                chapter = "Coordinate Geometry",
                title = "Director Circle Equations",
                formulaText = "Circle x² + y² = a²  -->  x² + y² = 2 a²\nEllipse x²/a² + y²/b² = 1  -->  x² + y² = a² + b²\nHyperbola x²/a² - y²/b² = 1  -->  x² + y² = a² - b²",
                description = "Locus of point of intersection of two mutually perpendicular tangents to a conic.",
                variablesDef = "a, b: Semi-major and semi-minor axes.",
                keyTip = "For rectangular hyperbola (a = b), director circle degenerates into the center (0,0)."
            ),
            FormulaEntity(
                subject = "MATHEMATICS",
                chapter = "Vectors & 3D",
                title = "Shortest Distance Between Skew Lines",
                formulaText = "d = | (a₂ - a₁) · (b₁ × b₂) | / | b₁ × b₂ |",
                description = "Calculates the minimum spatial clearance between two non-parallel, non-intersecting 3D lines r = a₁ + λ b₁ and r = a₂ + μ b₂.",
                variablesDef = "a₁, a₂: Position vectors of points on lines, b₁, b₂: Direction vectors.",
                keyTip = "If lines intersect, numerator is zero: (a₂ - a₁) · (b₁ × b₂) = 0 (Coplanar lines condition)."
            ),
            FormulaEntity(
                subject = "MATHEMATICS",
                chapter = "Algebra",
                title = "Cube Roots of Unity Properties",
                formulaText = "1 + ω + ω² = 0\nω³ = 1\nω = e^(i 2π/3), ω² = e^(i 4π/3)",
                description = "Key algebraic identities for non-real cube roots of unity.",
                variablesDef = "ω = (-1 + i√3)/2, ω² = (-1 - i√3)/2.",
                keyTip = "1 + ω^n + ω^(2n) equals 3 if n is a multiple of 3, and equals 0 otherwise."
            )
        )
    }

    fun getInitialTestResults(): List<TestResultEntity> {
        return listOf(
            TestResultEntity(
                testId = "mock_jee_01",
                testTitle = "JEE Main Full Mock Test #1",
                testType = "FULL_MOCK",
                totalMarks = 300,
                scoredMarks = 218,
                correctCount = 58,
                incorrectCount = 14,
                unattemptedCount = 3,
                accuracy = 80.5f,
                percentileEstimate = 99.18f,
                rankEstimate = 8420,
                timeTakenSeconds = 9840,
                completedAt = System.currentTimeMillis() - 86400000L * 4
            ),
            TestResultEntity(
                testId = "mock_phys_01",
                testTitle = "Physics Mechanics Part Test",
                testType = "PHYSICS_TEST",
                totalMarks = 100,
                scoredMarks = 82,
                correctCount = 21,
                incorrectCount = 2,
                unattemptedCount = 2,
                accuracy = 91.3f,
                percentileEstimate = 99.45f,
                rankEstimate = 5600,
                timeTakenSeconds = 3200,
                completedAt = System.currentTimeMillis() - 86400000L * 2
            )
        )
    }
}
