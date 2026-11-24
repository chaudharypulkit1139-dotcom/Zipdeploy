package com.example.margdarsh.data

/**
 * Curated syllabus-aligned starter tests for Classes 8 through 12.
 * Includes ongoing, upcoming, and previous tests with detailed explanations.
 */
object AcademicTestBank {

    private val now = System.currentTimeMillis()
    private val oneHour = 3600 * 1000L
    private val oneDay = 24 * 3600 * 1000L

    fun getInitialTests(): List<AcademicTest> {
        return listOf(
            // CLASS 10 - Mathematics (Ongoing)
            AcademicTest(
                id = "TEST_C10_MATH_01",
                targetClass = "10",
                subject = "Mathematics",
                title = "Quadratic Equations & Arithmetic Progressions",
                syllabus = "Standard form of quadratic equations, discriminant & nature of roots, nth term of an AP, sum of first n terms of an AP.",
                startTimeEpochMs = now - (2 * oneHour),
                endTimeEpochMs = now + (24 * oneHour),
                durationMinutes = 20,
                totalMarks = 5,
                questions = listOf(
                    TestQuestion(
                        id = "C10_M1",
                        question = "What is the discriminant of the quadratic equation 2x² - 4x + 3 = 0?",
                        options = listOf("-8", "8", "-4", "16"),
                        correctIndex = 0,
                        explanation = "Discriminant D = b² - 4ac. Here a = 2, b = -4, c = 3. So D = (-4)² - 4(2)(3) = 16 - 24 = -8. Since D < 0, roots are non-real complex.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C10_M2",
                        question = "If the 3rd term of an AP is 8 and the 7th term is 20, what is the common difference (d)?",
                        options = listOf("2", "3", "4", "5"),
                        correctIndex = 1,
                        explanation = "a3 = a + 2d = 8, a7 = a + 6d = 20. Subtracting equations: 4d = 12 => d = 3.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C10_M3",
                        question = "The roots of x² - 5x + 6 = 0 are:",
                        options = listOf("2 and 3", "-2 and -3", "1 and 6", "-1 and -6"),
                        correctIndex = 0,
                        explanation = "Factoring: x² - 2x - 3x + 6 = (x - 2)(x - 3) = 0. Therefore x = 2 or x = 3.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C10_M4",
                        question = "What is the sum of the first 10 terms of the AP: 2, 4, 6, 8, ...?",
                        options = listOf("90", "100", "110", "120"),
                        correctIndex = 2,
                        explanation = "Sn = n/2 * [2a + (n-1)d]. Here n = 10, a = 2, d = 2. S10 = 5 * [4 + 9(2)] = 5 * 22 = 110.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C10_M5",
                        question = "If a quadratic equation ax² + bx + c = 0 has two equal real roots, then:",
                        options = listOf("b² - 4ac > 0", "b² - 4ac = 0", "b² - 4ac < 0", "b² + 4ac = 0"),
                        correctIndex = 1,
                        explanation = "For equal and real roots, the discriminant D must be zero, i.e., b² - 4ac = 0.",
                        marks = 1
                    )
                )
            ),

            // CLASS 10 - Science (Upcoming)
            AcademicTest(
                id = "TEST_C10_SCI_02",
                targetClass = "10",
                subject = "Science",
                title = "Light: Reflection, Refraction & Human Eye",
                syllabus = "Laws of reflection, spherical mirrors, mirror formula, refractive index, lens formula, defects of vision and their correction.",
                startTimeEpochMs = now + (12 * oneHour),
                endTimeEpochMs = now + (36 * oneHour),
                durationMinutes = 25,
                totalMarks = 4,
                questions = listOf(
                    TestQuestion(
                        id = "C10_S1",
                        question = "Which type of mirror is used as a rear-view mirror in vehicles?",
                        options = listOf("Concave mirror", "Convex mirror", "Plane mirror", "Parabolic mirror"),
                        correctIndex = 1,
                        explanation = "Convex mirrors always produce an erect, diminished image and provide a wider field of view.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C10_S2",
                        question = "The power of a lens having focal length -0.5 m is:",
                        options = listOf("+2.0 D", "-2.0 D", "+0.5 D", "-0.5 D"),
                        correctIndex = 1,
                        explanation = "Power P = 1 / f (in metres). P = 1 / (-0.5) = -2.0 D. Negative power indicates a concave diverging lens.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C10_S3",
                        question = "Myopia (nearsightedness) can be corrected by using a lens with:",
                        options = listOf("Convex power", "Concave power", "Bifocal power", "Cylindrical power"),
                        correctIndex = 1,
                        explanation = "In myopia, light rays focus before reaching the retina. A diverging (concave) lens moves the focal point back onto the retina.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C10_S4",
                        question = "The phenomenon responsible for the twinkling of stars is:",
                        options = listOf("Total internal reflection", "Atmospheric refraction", "Dispersion of light", "Scattering of light"),
                        correctIndex = 1,
                        explanation = "Atmospheric refraction occurs due to varying temperature and optical densities of atmospheric layers, making star positions fluctuate continuously.",
                        marks = 1
                    )
                )
            ),

            // CLASS 10 - Previous Test
            AcademicTest(
                id = "TEST_C10_SST_03",
                targetClass = "10",
                subject = "Social Science",
                title = "Nationalism in India & Resource Development",
                syllabus = "Non-Cooperation Movement, Civil Disobedience, Salt March, Types of Resources, Soil conservation techniques.",
                startTimeEpochMs = now - (48 * oneHour),
                endTimeEpochMs = now - (6 * oneHour),
                durationMinutes = 15,
                totalMarks = 3,
                questions = listOf(
                    TestQuestion(
                        id = "C10_SS1",
                        question = "In which year did the historic Dandi Salt March begin?",
                        options = listOf("1928", "1930", "1931", "1942"),
                        correctIndex = 1,
                        explanation = "Mahatma Gandhi began the Salt March from Sabarmati Ashram to Dandi on March 12, 1930, marking the start of the Civil Disobedience Movement.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C10_SS2",
                        question = "Black soil is predominantly found in which region of India?",
                        options = listOf("Northern Plains", "Deccan Trap (Plateau)", "Eastern Coastal Plains", "Thar Desert"),
                        correctIndex = 1,
                        explanation = "Black soil (Regur soil) covers the Deccan trap region (Maharashtra, Gujarat, Madhya Pradesh) and is ideal for cotton cultivation.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C10_SS3",
                        question = "The Jallianwala Bagh incident took place on which festival day?",
                        options = listOf("Diwali", "Holi", "Baisakhi", "Dussehra"),
                        correctIndex = 2,
                        explanation = "General Dyer ordered firing on an unarmed peaceful gathering at Jallianwala Bagh, Amritsar on the day of Baisakhi, 13 April 1919.",
                        marks = 1
                    )
                )
            ),

            // CLASS 9 - Ongoing Test
            AcademicTest(
                id = "TEST_C9_SCI_01",
                targetClass = "9",
                subject = "Science",
                title = "Laws of Motion & Gravitation",
                syllabus = "Newton's three laws of motion, inertia, momentum, universal law of gravitation, acceleration due to gravity (g).",
                startTimeEpochMs = now - (1 * oneHour),
                endTimeEpochMs = now + (20 * oneHour),
                durationMinutes = 20,
                totalMarks = 4,
                questions = listOf(
                    TestQuestion(
                        id = "C9_S1",
                        question = "The inertia of an object tends to cause the object to:",
                        options = listOf("Increase its speed", "Decrease its momentum", "Resist any change in its state of motion", "Decelerate due to friction"),
                        correctIndex = 2,
                        explanation = "Inertia is the natural tendency of an object to resist any change in its state of rest or uniform motion (Newton's 1st Law).",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C9_S2",
                        question = "The value of acceleration due to gravity (g) at the surface of Earth is approximately:",
                        options = listOf("9.8 m/s²", "9.8 N", "6.67 × 10⁻¹¹ m/s²", "8.9 m/s²"),
                        correctIndex = 0,
                        explanation = "On Earth's surface, g = GM / R² ≈ 9.8 m/s² directed towards the Earth's centre.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C9_S3",
                        question = "Rate of change of momentum is directly proportional to:",
                        options = listOf("Applied unbalanced force", "Inertia", "Velocity", "Displacement"),
                        correctIndex = 0,
                        explanation = "Newton's Second Law states that force F = dp/dt = m*a (for constant mass).",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C9_S4",
                        question = "Action and reaction forces mentioned in Newton's Third Law:",
                        options = listOf("Act on the same body", "Act on different bodies in opposite directions", "Are unequal in magnitude", "Cancel each other out"),
                        correctIndex = 1,
                        explanation = "Action and reaction always act simultaneously on two different interacting bodies, so they never cancel each other.",
                        marks = 1
                    )
                )
            ),

            // CLASS 11 - Ongoing Test (Physics & Chemistry)
            AcademicTest(
                id = "TEST_C11_PHY_01",
                targetClass = "11",
                subject = "Physics",
                title = "Kinematics & Work, Energy, Power",
                syllabus = "Vectors, projectile motion, relative velocity, work-energy theorem, conservative forces, elastic and inelastic collisions.",
                startTimeEpochMs = now - (3 * oneHour),
                endTimeEpochMs = now + (30 * oneHour),
                durationMinutes = 25,
                totalMarks = 4,
                questions = listOf(
                    TestQuestion(
                        id = "C11_P1",
                        question = "For a projectile launched at angle θ with speed u, the horizontal range is maximum when θ equals:",
                        options = listOf("30°", "45°", "60°", "90°"),
                        correctIndex = 1,
                        explanation = "Range R = (u² sin 2θ) / g. Maximum value occurs when sin 2θ = 1 => 2θ = 90° => θ = 45°.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C11_P2",
                        question = "Work done by a conservative force along any closed circular loop is:",
                        options = listOf("Zero", "Positive", "Negative", "Depends on radius"),
                        correctIndex = 0,
                        explanation = "By definition, the work done by a conservative force (e.g., gravity, electrostatic) around any closed path is identically zero.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C11_P3",
                        question = "In an elastic collision between two bodies:",
                        options = listOf("Only momentum is conserved", "Only kinetic energy is conserved", "Both momentum and kinetic energy are conserved", "Neither is conserved"),
                        correctIndex = 2,
                        explanation = "In perfectly elastic collisions, total linear momentum AND total mechanical kinetic energy are both strictly conserved.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C11_P4",
                        question = "The dimension formula for Planck's constant (h) is identical to that of:",
                        options = listOf("Linear momentum", "Angular momentum", "Torque", "Power"),
                        correctIndex = 1,
                        explanation = "Planck's constant has units of J·s (M L² T⁻¹), which matches the dimensional formula of Angular Momentum (L = r × p).",
                        marks = 1
                    )
                )
            ),

            // CLASS 12 - Ongoing Test (Physics / Chemistry / Biology)
            AcademicTest(
                id = "TEST_C12_PHY_01",
                targetClass = "12",
                subject = "Physics",
                title = "Electrostatics & Current Electricity",
                syllabus = "Coulomb's Law, Electric flux, Gauss theorem, Capacitance, Kirchhoff's laws, Wheatstone bridge, Drift velocity.",
                startTimeEpochMs = now - (2 * oneHour),
                endTimeEpochMs = now + (28 * oneHour),
                durationMinutes = 30,
                totalMarks = 4,
                questions = listOf(
                    TestQuestion(
                        id = "C12_P1",
                        question = "The electric field inside a uniformly charged conducting spherical shell is:",
                        options = listOf("Zero", "Maximum at center", "Increases linearly with distance", "Infinite"),
                        correctIndex = 0,
                        explanation = "By Gauss's law, enclosing zero charge inside any Gaussian surface inside the conductor yields E = 0 everywhere within the shell.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C12_P2",
                        question = "Kirchhoff's First Rule (Junction rule) is based on the conservation of:",
                        options = listOf("Energy", "Charge", "Momentum", "Mass"),
                        correctIndex = 1,
                        explanation = "The junction law (Σ I = 0) is a direct consequence of the conservation of electric charge (charge cannot accumulate at an ideal junction).",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C12_P3",
                        question = "When a dielectric slab of constant K is inserted between the plates of an isolated charged capacitor, the capacitance:",
                        options = listOf("Decreases by factor K", "Increases by factor K", "Remains unchanged", "Becomes zero"),
                        correctIndex = 1,
                        explanation = "Capacitance increases to C = K * C0 because dielectric polarization reduces the net electric field between plates.",
                        marks = 1
                    ),
                    TestQuestion(
                        id = "C12_P4",
                        question = "Drift velocity of free electrons in a conductor is proportional to:",
                        options = listOf("Applied electric field E", "Square of electric field E²", "Square root of E", "Inversely to E"),
                        correctIndex = 0,
                        explanation = "vd = (e * E * τ) / m. Therefore drift velocity vd is directly proportional to electric field strength E.",
                        marks = 1
                    )
                )
            )
        )
    }
}
