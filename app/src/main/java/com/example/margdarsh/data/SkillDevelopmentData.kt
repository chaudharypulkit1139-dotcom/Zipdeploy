package com.example.margdarsh.data

data class SkillItem(
    val id: String,
    val title: String,
    val category: String,
    val timeToLearn: String,
    val highIncomeRole: String,
    val summary: String,
    val whyImportant: String,
    val actionSteps: List<String>,
    val recommendedTools: List<String>,
    val freeResources: List<String>
)

val TOP_10_SKILLS = listOf(
    SkillItem(
        id = "ai_prompting",
        title = "AI Prompting & Practical GenAI Tools",
        category = "Tech & Future of Work",
        timeToLearn = "4 to 6 Weeks",
        highIncomeRole = "AI Workflow Specialist / Product Analyst",
        summary = "Master generative AI workflows with Gemini, ChatGPT, Claude, and automation pipelines to multiply productivity 10x.",
        whyImportant = "By 2026-2030, professionals who effectively leverage AI copilots will replace those who do not, across law, finance, coding, and marketing.",
        actionSteps = listOf(
            "Learn structured prompt framing (Context, Persona, Constraints, Output Format).",
            "Build automation tasks connecting Google Workspace with Gemini APIs.",
            "Complete free foundational AI certifications from Google Cloud & DeepLearning.AI.",
            "Create a personal portfolio of 3 automated AI productivity workflows."
        ),
        recommendedTools = listOf("Gemini 1.5/2.0", "Claude 3.5 Sonnet", "Notion AI", "Make.com / Zapier"),
        freeResources = listOf("Google AI Essentials", "DeepLearning.AI Prompt Engineering", "Fast.ai Practical Deep Learning")
    ),
    SkillItem(
        id = "financial_literacy",
        title = "Financial Intelligence & Wealth Management",
        category = "Finance & Business",
        timeToLearn = "6 to 8 Weeks",
        highIncomeRole = "Financial Analyst / Equity Research / Wealth Strategist",
        summary = "Understand personal budgeting, equity markets, compounding, balance sheet audits, and taxation frameworks.",
        whyImportant = "School curricula rarely teach money management. Financial literacy prevents debt traps and builds long-term generational wealth.",
        actionSteps = listOf(
            "Learn to read balance sheets, P&L statements, and cash flows of Indian companies on Screener.in.",
            "Understand mutual funds, index investing (Nifty 50), and debt instruments.",
            "Study Indian income tax slabs, 80C exemptions, and GST basics.",
            "Simulate a virtual equity portfolio using NSE Paathshaala / Moneybhai."
        ),
        recommendedTools = listOf("Screener.in", "TradingView", "Zerodha Varsity", "Tickertape"),
        freeResources = listOf("Zerodha Varsity Modules 1-4", "SEBI Investor Education Portal", "Khan Academy Finance")
    ),
    SkillItem(
        id = "public_speaking",
        title = "High-Stakes Public Speaking & Persuasion",
        category = "Leadership & Influence",
        timeToLearn = "8 Weeks",
        highIncomeRole = "Corporate Spokesperson / Management Consultant / Founder",
        summary = "Captivate audiences, structure arguments with clarity, pitch ideas with executive presence, and handle debate rebuttal.",
        whyImportant = "Top careers in corporate management, civil services, and law reward individuals who can articulate complex ideas with confidence.",
        actionSteps = listOf(
            "Record 2-minute impromptu speeches daily on random topics to eliminate filler words.",
            "Practice the SCQA framework (Situation, Complication, Question, Answer).",
            "Participate in local school/college debates and Model United Nations (MUN).",
            "Study speeches of top communicators like Steve Jobs and Dr. S. Jaishankar."
        ),
        recommendedTools = listOf("Orai AI Speech Coach", "Teleprompter App", "Voice Memos"),
        freeResources = listOf("Toastmasters International Guides", "Harvard Division of Continuing Education Speech Tips")
    ),
    SkillItem(
        id = "data_analytics",
        title = "Data Analytics & Business Intelligence",
        category = "Analytics & Tech",
        timeToLearn = "10 Weeks",
        highIncomeRole = "Business Data Analyst / Operations Strategist",
        summary = "Transform raw business data into actionable dashboards using Excel/Google Sheets, SQL queries, and Power BI.",
        whyImportant = "Every modern Indian startup, FMCG, and bank relies on data analysts to optimize customer acquisition and unit economics.",
        actionSteps = listOf(
            "Master advanced Excel formulas (XLOOKUP, INDEX-MATCH, Pivot Tables, Conditional Modeling).",
            "Learn fundamental SQL queries (SELECT, GROUP BY, JOINs, Subqueries).",
            "Build an interactive visual dashboard in Google Looker Studio or Microsoft Power BI.",
            "Analyze a public Indian Census or RBI dataset and present findings."
        ),
        recommendedTools = listOf("Google Sheets / Excel", "PostgreSQL / SQLite", "Google Looker Studio", "Tableau Public"),
        freeResources = listOf("Google Data Analytics Professional Certificate (Audit)", "Mode Analytics SQL Tutorial")
    ),
    SkillItem(
        id = "coding_python",
        title = "Python Programming & Algorithmic Thinking",
        category = "Software & Engineering",
        timeToLearn = "12 Weeks",
        highIncomeRole = "Software Engineer / Data Scientist / Fintech Developer",
        summary = "Learn core programming constructs, object-oriented concepts, and problem-solving through clean Python code.",
        whyImportant = "Python is the undisputed language for AI, data science, automation, and backend development worldwide.",
        actionSteps = listOf(
            "Learn Python data types, control flow, loops, and functions.",
            "Solve 50 easy/medium problems on LeetCode or HackerRank to build logic.",
            "Write web scrapers with BeautifulSoup to extract real-world information.",
            "Build a full project (e.g., student grade tracker or automated expense bot)."
        ),
        recommendedTools = listOf("VS Code", "Python 3.12", "Jupyter Notebooks", "GitHub"),
        freeResources = listOf("CS50P: Harvard's Python Course", "Automate the Boring Stuff with Python", "Corey Schafer YouTube")
    ),
    SkillItem(
        id = "corporate_law",
        title = "Contract Literacy & Corporate Governance",
        category = "Legal & Compliance",
        timeToLearn = "6 Weeks",
        highIncomeRole = "Corporate Legal Advisor / Compliance Officer / M&A Analyst",
        summary = "Understand commercial contracts, Non-Disclosure Agreements (NDAs), intellectual property, and Indian Companies Act essentials.",
        whyImportant = "Modern business operations require constant legal vigilance. Understanding agreements protects rights and opens lucrative corporate advisory roles.",
        actionSteps = listOf(
            "Read standard boilerplate clauses (Indemnity, Force Majeure, Jurisdiction, Termination).",
            "Understand trademark and copyright registration via Indian IP India portal.",
            "Study landmark Supreme Court business arbitration case summaries.",
            "Draft a sample service agreement for freelance work."
        ),
        recommendedTools = listOf("Indian Kanoon", "Ministry of Corporate Affairs (MCA) Portal", "LiveLaw"),
        freeResources = listOf("iPleaders Law Blog", "NPTEL Corporate Law Lectures by NLUs")
    ),
    SkillItem(
        id = "digital_marketing",
        title = "Performance Marketing & Brand Storytelling",
        category = "Marketing & Media",
        timeToLearn = "6 Weeks",
        highIncomeRole = "Growth Marketer / Brand Strategist / Performance Specialist",
        summary = "Drive customer acquisition through Search Engine Optimization (SEO), Meta & Google Ads, and narrative copywriting.",
        whyImportant = "Consumer brands and service companies spend billions on digital channels; high-performing growth marketers command elite packages.",
        actionSteps = listOf(
            "Learn SEO basics: keyword intent, on-page optimization, and backlink authority.",
            "Understand ROAS (Return on Ad Spend), CAC (Customer Acquisition Cost), and LTV.",
            "Write 10 compelling conversion copywriting ads for a mock product.",
            "Launch an experimental organic blog or social channel with measured weekly growth."
        ),
        recommendedTools = listOf("Google Search Console", "Meta Ads Manager", "Canva Pro", "Ahrefs Webmaster Tools"),
        freeResources = listOf("HubSpot Inbound Marketing Certification", "Google Digital Garage")
    ),
    SkillItem(
        id = "critical_problem_solving",
        title = "First-Principles Thinking & Case Solving",
        category = "Management & Consulting",
        timeToLearn = "8 Weeks",
        highIncomeRole = "Management Consultant (McKinsey/BCG/Bain) / Product Manager",
        summary = "Deconstruct ambiguous business problems into MECE (Mutually Exclusive, Collectively Exhaustive) issue trees and hypothesis testing.",
        whyImportant = "Top consulting firms and executive roles select candidates purely based on their structured analytical problem-solving skills.",
        actionSteps = listOf(
            "Master market sizing and guesstimate techniques (e.g., 'Estimate Delhi metro daily ridership').",
            "Learn profitability frameworks: Revenue (Price x Volume) vs. Costs (Fixed + Variable).",
            "Read and solve 20 business case studies from IIM / Harvard case books.",
            "Practice mock case interviews with peers under 30-minute time limits."
        ),
        recommendedTools = listOf("Miro / FigJam", "MindMeister", "Notion Frameworks"),
        freeResources = listOf("Case Interview Secrets by Victor Cheng", "Harvard Business Publishing Student Resources")
    ),
    SkillItem(
        id = "ui_ux_design",
        title = "UI/UX & Human-Centered Product Design",
        category = "Design & Creative",
        timeToLearn = "8 Weeks",
        highIncomeRole = "Product Designer / UI/UX Lead",
        summary = "Design intuitive digital interfaces, wireframes, interactive prototypes, and conduct usability research.",
        whyImportant = "Product usability decides the success of software and mobile apps. Skilled designers bridge user psychology and engineering.",
        actionSteps = listOf(
            "Learn Figma shortcuts, auto-layout, design tokens, and components.",
            "Study Nielsen Norman Group's 10 Usability Heuristics.",
            "Redesign an existing confusing Indian government or banking app interface.",
            "Publish a documented case study on Behance or Dribbble explaining your design decisions."
        ),
        recommendedTools = listOf("Figma", "FigJam", "Mobbin", "Balsamiq"),
        freeResources = listOf("Google UX Design Certificate (Coursera Audit)", "Figma YouTube Tutorials", "Laws of UX")
    ),
    SkillItem(
        id = "negotiation",
        title = "Strategic Negotiation & Conflict Resolution",
        category = "Executive & Soft Skills",
        timeToLearn = "4 Weeks",
        highIncomeRole = "Dealmaker / Procurement Head / Senior Diplomat",
        summary = "Discover BATNA (Best Alternative to a Negotiated Agreement), emotional intelligence in conflict, and value-expansion tactics.",
        whyImportant = "Whether negotiating salary, vendor contracts, or partnership stakes, superior negotiators gain hundreds of thousands in lifetime earnings.",
        actionSteps = listOf(
            "Understand the difference between positional bargaining and principled negotiation.",
            "Learn the tactics from Chris Voss ('Never Split the Difference') and Harvard PON.",
            "Role-play salary negotiation scenarios with specific anchor numbers.",
            "Practice tactical silence and open-ended calibrated questions (How / What)."
        ),
        recommendedTools = listOf("Otter.ai for transcript review", "Harvard Program on Negotiation Case Briefs"),
        freeResources = listOf("Harvard Program on Negotiation (PON) Articles", "Chris Voss Masterclass Summaries")
    )
)
