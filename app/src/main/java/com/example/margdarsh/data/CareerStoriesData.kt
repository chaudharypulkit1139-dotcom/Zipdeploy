package com.example.margdarsh.data

data class CareerStoryScheduleItem(
    val time: String,
    val activityTitle: String,
    val description: String
)

data class DayInTheLifeStory(
    val id: String,
    val careerTitle: String,
    val industry: String,
    val roleBadge: String,
    val coreMission: String,
    val schedule: List<CareerStoryScheduleItem>,
    val highSchoolProTip: String,
    val keyToolsUsed: List<String>,
    val averageCompRange: String,
    val intellectualExcitement: String
)

val DAY_IN_THE_LIFE_STORIES = listOf(
    DayInTheLifeStory(
        id = "data_scientist",
        careerTitle = "Data Scientist & AI Architect",
        industry = "Artificial Intelligence & Big Tech",
        roleBadge = "Tech & Predictive Modeling",
        coreMission = "Designing intelligent neural networks and predictive algorithms that convert massive unstructured datasets into automated decision engines.",
        schedule = listOf(
            CareerStoryScheduleItem(
                time = "09:30 AM",
                activityTitle = "Engineering Standup & Cluster Health Check",
                description = "Review GPU model training runs from overnight cluster jobs. Analyze loss curves and check inference latency across production APIs."
            ),
            CareerStoryScheduleItem(
                time = "11:00 AM",
                activityTitle = "Deep Learning Architecture & Feature Engineering",
                description = "Code in Python & PyTorch to design custom transformer attention layers. Clean and vectorize 10M+ real-time customer behavioral tokens."
            ),
            CareerStoryScheduleItem(
                time = "02:00 PM",
                activityTitle = "Product Strategy & Fraud Heuristics Sync",
                description = "Collaborate with Product Managers and Risk Directors to evaluate model accuracy vs. false positive tradeoffs on payments."
            ),
            CareerStoryScheduleItem(
                time = "04:30 PM",
                activityTitle = "Hyperparameter Tuning & Automated MLOps Pipeline",
                description = "Optimize weights with Bayesian search, package containerized Docker images, and deploy live to AWS/GCP Kubernetes clusters."
            ),
            CareerStoryScheduleItem(
                time = "06:00 PM",
                activityTitle = "ArXiv Research Paper Exploration",
                description = "Read newly published papers from NeurIPS and ICML to evaluate if cutting-edge diffusion or LLM techniques improve existing pipelines."
            )
        ),
        highSchoolProTip = "Master Linear Algebra, Probability, and Python early. Real-world AI engineering is not rote memorization—it's creative mathematical problem solving.",
        keyToolsUsed = listOf("Python 3.12", "PyTorch / TensorFlow", "Docker / Kubernetes", "SQL / BigQuery", "Weights & Biases"),
        averageCompRange = "₹16 LPA (Entry) → ₹38 LPA (Mid) → ₹75+ LPA (Lead)",
        intellectualExcitement = "Watching an algorithm you coded make hundreds of accurate real-time predictions every single second for millions of people worldwide."
    ),
    DayInTheLifeStory(
        id = "product_designer",
        careerTitle = "Digital Product Designer (UI/UX)",
        industry = "Consumer Apps, SaaS & Design Systems",
        roleBadge = "Creative Tech & Psychology",
        coreMission = "Architecting seamless, beautiful digital experiences that make complex technology effortless, accessible, and delightful for millions of human beings.",
        schedule = listOf(
            CareerStoryScheduleItem(
                time = "10:00 AM",
                activityTitle = "User Journey & Drop-off Heatmap Audit",
                description = "Analyze Amplitude and FullStory analytics to pinpoint where users encounter cognitive confusion during app onboarding."
            ),
            CareerStoryScheduleItem(
                time = "11:30 AM",
                activityTitle = "Rapid Wireframing & Design System Scaling",
                description = "Design high-fidelity interactive component libraries and fluid layout prototypes in Figma using strict 8pt grid systems."
            ),
            CareerStoryScheduleItem(
                time = "02:30 PM",
                activityTitle = "Live Usability Testing Interviews",
                description = "Conduct 1-on-1 moderated user feedback sessions with 4 target consumers to observe facial reactions and navigational hesitations."
            ),
            CareerStoryScheduleItem(
                time = "04:30 PM",
                activityTitle = "Engineering Design Critique & Handoff",
                description = "Review interactive micro-animations, color contrast tokens, and edge cases with iOS/Android frontend development leads."
            ),
            CareerStoryScheduleItem(
                time = "06:30 PM",
                activityTitle = "Spatial Computing & Typography Exploration",
                description = "Experiment with emerging AR/VR spatial interfaces and refine variable typography pairings for maximum emotional resonance."
            )
        ),
        highSchoolProTip = "Great design is empathy plus psychology. Start observing why certain apps feel effortless while others feel frustrating, and recreate your favorite UIs.",
        keyToolsUsed = listOf("Figma", "ProtoPie", "Framer", "Design Tokens", "UserTesting.com"),
        averageCompRange = "₹12 LPA (Entry) → ₹28 LPA (Mid) → ₹55+ LPA (Principal)",
        intellectualExcitement = "The power to transform frustrating, clunky software into an elegant tool that millions of people love using every single day."
    ),
    DayInTheLifeStory(
        id = "corporate_lawyer",
        careerTitle = "Corporate & M&A Lawyer",
        industry = "Tier-1 Law Firms, Private Equity & Tech Giants",
        roleBadge = "High-Stakes Corporate Law",
        coreMission = "Safeguarding multi-million dollar corporate transactions, negotiating shareholder pacts, and structuring strategic commercial contracts.",
        schedule = listOf(
            CareerStoryScheduleItem(
                time = "09:00 AM",
                activityTitle = "Due Diligence Audit on ₹400 Cr Acquisition",
                description = "Review corporate filings, board minutes, and regulatory clearances for an ongoing cross-border technology merger."
            ),
            CareerStoryScheduleItem(
                time = "11:30 AM",
                activityTitle = "Drafting Share Purchase Agreements (SPA)",
                description = "Draft airtight indemnity covenants, non-compete clauses, and liquidation preference terms for international venture investors."
            ),
            CareerStoryScheduleItem(
                time = "02:00 PM",
                activityTitle = "High-Stakes Negotiation Conference",
                description = "Participate in a 3-hour negotiation call with opposing counsel from London/Singapore to resolve contentious closing conditions."
            ),
            CareerStoryScheduleItem(
                time = "05:00 PM",
                activityTitle = "Regulatory Filings with SEBI & CCI",
                description = "Advise client executives on statutory compliances under Competition Commission of India regulations and Companies Act."
            ),
            CareerStoryScheduleItem(
                time = "07:30 PM",
                activityTitle = "Commercial Case Law Research",
                description = "Analyze landmark Supreme Court and Delaware corporate precedent rulings to formulate defense strategies against shareholder disputes."
            )
        ),
        highSchoolProTip = "Build exceptional reading speed, precise vocabulary, and argument deconstruction through formal debates, MUNs, and critical editorial reading.",
        keyToolsUsed = listOf("Manupatra / SCC Online", "Contract Lifecycle AI", "DocuSign", "Virtual Data Rooms", "LexisNexis"),
        averageCompRange = "₹15 LPA (Entry) → ₹40 LPA (Mid) → ₹1+ Cr (Partner)",
        intellectualExcitement = "Being in the inner war room where multi-million dollar business destinies and major industry acquisitions are decided."
    ),
    DayInTheLifeStory(
        id = "investment_banker",
        careerTitle = "Investment Banker & Capital Strategist",
        industry = "Global Investment Banking & Private Capital",
        roleBadge = "Global Finance & Valuation",
        coreMission = "Valuing high-growth companies, structuring Initial Public Offerings (IPOs), and advising corporate boards on capital allocation and debt restructuring.",
        schedule = listOf(
            CareerStoryScheduleItem(
                time = "08:30 AM",
                activityTitle = "Global Market Briefing & Macroeconomic Scan",
                description = "Review Asian and US market closing numbers, bond yields, crude prices, and RBI interest rate policy statements."
            ),
            CareerStoryScheduleItem(
                time = "10:30 AM",
                activityTitle = "Discounted Cash Flow (DCF) & LBO Valuation",
                description = "Build a dynamic 15-tab financial model in Excel modeling 5-year revenue projections, working capital cycles, and EBITDA exit multiples."
            ),
            CareerStoryScheduleItem(
                time = "01:30 PM",
                activityTitle = "IPO Pitch Deck & Roadshow Material Review",
                description = "Fine-tune confidential investment memorandums and investor presentation slides for an upcoming ₹1,200 Cr public listing."
            ),
            CareerStoryScheduleItem(
                time = "04:30 PM",
                activityTitle = "Client CFO & Forensic Audit Due Diligence",
                description = "Lead structured interrogations with the target company's finance leadership on recurring cash flows and debt covenants."
            ),
            CareerStoryScheduleItem(
                time = "07:00 PM",
                activityTitle = "Scenario Sensitivity & Stress Testing",
                description = "Simulate currency depreciation and interest rate shock scenarios to ensure proposed acquisition financing structures remain resilient."
            )
        ),
        highSchoolProTip = "Master accounting fundamentals, financial statement analysis, and compound interest models early. Finance is the universal language of enterprise.",
        keyToolsUsed = listOf("Excel Financial Modeling", "Bloomberg Terminal", "FactSet", "PitchBook", "S&P Capital IQ"),
        averageCompRange = "₹18 LPA (Entry) → ₹45 LPA (Mid) → ₹1.2+ Cr (Managing Director)",
        intellectualExcitement = "Standing at the intersection of capital and ambition, orchestrating deals that build industry giants."
    )
)
