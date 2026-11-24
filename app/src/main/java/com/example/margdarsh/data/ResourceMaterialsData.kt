package com.example.margdarsh.data

data class ResourceMaterial(
    val id: String,
    val title: String,
    val subtitle: String,
    val pagesCount: Int,
    val fileSizeMb: String,
    val fileType: String,
    val assetFileName: String,
    val category: String,
    val highlightBadge: String,
    val targetAudience: String,
    val sampleChapters: List<String>,
    val keyTakeaways: List<String>,
    val detailedSummary: String
)

val LOCKED_PARENT_RESOURCES = listOf(
    ResourceMaterial(
        id = "careers_after_school",
        title = "Careers After School: Master Career Encyclopedia",
        subtitle = "Comprehensive directory of 100+ careers, engineering, medical, defense, law, civil services & design",
        pagesCount = 110,
        fileSizeMb = "5.4 MB",
        fileType = "PDF",
        assetFileName = "careers_after_school.pdf",
        category = "Complete Career Master Guide",
        highlightBadge = "★ 100+ CAREERS INCLUDED",
        targetAudience = "Class 9-12 Students & Parents exploring all streams & career options",
        sampleChapters = listOf(
            "National & State Entrance Exam Calendars",
            "Science, Tech, Bio-sciences & Allied Medicine",
            "Corporate Finance, Analytics & Management Streams",
            "Defense Forces (NDA/CDS), Law (CLAT) & Design (NID/UCEED)",
            "Top Tier-1 Government & Private University Directory"
        ),
        keyTakeaways = listOf(
            "Exhaustive coverage of 100+ verified modern careers in India & abroad.",
            "Detailed entrance exam paths, eligibility criteria, and cutoffs.",
            "Salary benchmarks, growth trajectories, and 5-year hiring industry outlook.",
            "Comprehensive directory of top accredited colleges across India."
        ),
        detailedSummary = "The ultimate 110-page master handbook for Indian students and parents. Provides complete clarity on course duration, eligibility, entrance tests, top institutes, and starting compensation for over 100 professional careers spanning all streams."
    ),
    ResourceMaterial(
        id = "after_12th_commerce",
        title = "After XII Commerce: 75+ Degrees & College Directory",
        subtitle = "Detailed analysis of 75+ professional degrees, top tier-1 colleges (SRCC, NMIMS, Xavier's), and high-paying jobs",
        pagesCount = 77,
        fileSizeMb = "3.2 MB",
        fileType = "PDF",
        assetFileName = "career_after_commerce.pdf",
        category = "Commerce, Finance, Management & Law",
        highlightBadge = "★ 75+ DEGREES & TIER-1 CUTOFFS",
        targetAudience = "Class 10-12 Students & Parents considering Commerce, Finance & Management",
        sampleChapters = listOf(
            "B.Com (Hons), BBA, BAF, BFM & Business Analytics Pathways",
            "Professional Certifications: CA, CS, CMA, CFA & Actuarial Science",
            "Tier-1 Colleges Directory: SRCC, St. Xavier's, NMIMS, Christ & NLUs",
            "Investment Banking, FinTech & Corporate Law Career Blueprints"
        ),
        keyTakeaways = listOf(
            "Complete directory of 75+ accredited professional degrees and specializations.",
            "Top colleges mapped across India with cutoffs and admission channels.",
            "Salary benchmarks for Investment Banking, Risk, Forensic Accounting, and FinTech.",
            "Entrance exam timelines for IPMAT, CUET, SET, NPAT, and CLAT."
        ),
        detailedSummary = "A 77-page comprehensive publication covering every major specialization after Class 12 Commerce: Banking & Insurance, Financial Markets, Business Analytics, International Accounting, Actuarial Science, Corporate Law, and Digital Marketing."
    ),
    ResourceMaterial(
        id = "neutral_career_options",
        title = "Neutral Career Options (Open to Any Stream)",
        subtitle = "Elite high-paying careers open to students from Science, Commerce, and Arts alike",
        pagesCount = 45,
        fileSizeMb = "5.7 MB",
        fileType = "PDF",
        assetFileName = "neutral_career_options.pdf",
        category = "Stream-Neutral Opportunities",
        highlightBadge = "★ NO SCIENCE/COMMERCE LOCK-IN",
        targetAudience = "Students uncertain about stream lock-in or seeking creative/high-growth options",
        sampleChapters = listOf(
            "Industrial, Communication, UI/UX & Fashion Design (B.Des)",
            "5-Year Integrated Corporate Law (BA/BBA LLB via CLAT & AILET)",
            "Journalism, Digital Media, Filmmaking & Mass Communication",
            "Economics (Hons), Liberal Arts, Psychology & Hospitality Management",
            "Portfolio Creation, Studio Tests & Entrance Exam Roadmaps"
        ),
        keyTakeaways = listOf(
            "Breakdown of high-paying neutral careers: Design (B.Des), Law, Media, Economics, and Hospitality.",
            "Top entrance exam roadmaps: UCEED, NID DAT, NIFT, CLAT, AILET, and SET.",
            "Directory of elite institutes: NID Ahmedabad, IIT Bombay (IDC), Ashoka, OP Jindal, and Symbiosis.",
            "Step-by-step studio tests, portfolio tips, and interview preparation strategies."
        ),
        detailedSummary = "A parent's master guide proving that career success is not limited to Engineering and Medical. Reveals lucrative stream-neutral pathways like Industrial Design, Corporate Law, Economics Honours, and Hospitality Management with full admission schedules."
    ),
    ResourceMaterial(
        id = "careers_in_science",
        title = "Careers in Science & Modern Tech Master Blueprint",
        subtitle = "Comprehensive blueprint of Pure Sciences, Applied Tech, Biotechnology, AI/ML & Research",
        pagesCount = 65,
        fileSizeMb = "2.6 MB",
        fileType = "PPTX",
        assetFileName = "careers_in_science.pptx",
        category = "Science, Technology & Research",
        highlightBadge = "★ PURE & APPLIED SCIENCES",
        targetAudience = "Class 10-12 Science (PCM/PCB) students & parents exploring future-proof tech",
        sampleChapters = listOf(
            "Pure vs. Applied Sciences: Physics, Chemistry, Mathematics & Earth Sciences",
            "Emerging Frontiers: Biotechnology, Bioinformatics & Genetics",
            "AI/ML, Data Science, Quantum Computing & Cybersecurity Pathways",
            "Premier Research Institutes: IISc Bangalore, IISERs, NISER, IITs & AIIMS",
            "Fellowships, KVPY/INSPIRE Grants & Global PhD Trajectories"
        ),
        keyTakeaways = listOf(
            "Comprehensive breakdown of 40+ specialized science and tech disciplines.",
            "Pathways to premier Indian research institutes (IISc, IISERs, NISER, IITs, TIFR).",
            "Emerging high-demand careers in Biotech, Data Science, AI, and Space Tech.",
            "Fellowship calendars, funding opportunities, and corporate R&D salaries."
        ),
        detailedSummary = "A 65-slide comprehensive master deck breaking down the vast landscape of Science & Technology careers. Goes far beyond traditional engineering and MBBS to reveal cutting-edge opportunities in Bioinformatics, Artificial Intelligence, Aerospace, and Fundamental Research."
    )
)
