package com.example.margdarsh.util

object GoogleSheetAppsScriptTemplate {

    val SCRIPT_CODE: String = """
/**
 * =========================================================================
 * MARGDARSH CAREER INTELLIGENCE - ENTERPRISE 4-SHEETS & DRIVE CLOUD BACKEND
 * =========================================================================
 * 
 * 4 AUTOMATIC DEDICATED SHEETS (ZERO DUPLICATES, PURE PLAIN TEXT):
 * 1. 'Student Registrations & Profiles' : 1 student = 1 row upsert engine (clean text phones)
 * 2. 'Payment Records & Drive Proofs'  : Service bought, UTR & Drive proof links (Deduplicated payments)
 * 3. '1-on-1 Counselling Students'     : Dedicated roster for personal mentoring sessions (Deduplicated)
 * 4. 'Coupons Creation & Usage'        : Coupons creation, discount %, max limits & usage counts
 *
 * FIXES IN THIS VERSION:
 * ✓ FIXED: Single students no longer counted multiple times in Paid Orders & Counselling.
 * ✓ FIXED: Strict transaction deduplication by UTR and (Student + Service) to protect revenue math.
 * ✓ FIXED: Phone numbers and codes stored strictly as PLAIN TEXT (' prefix + @ format),
 *          preventing Google Sheets from evaluating them as functions/formulas (#ERROR! / #NAME?).
 * ✓ ADDED: Auto-repair & 1-click Deduplication tool to clean existing duplicate rows in the sheet.
 *
 * HOW TO UPDATE / DEPLOY IN 2 MINUTES:
 * 1. Open your Google Sheet -> Click Extensions > Apps Script
 * 2. Select all existing code in Code.gs, delete it, and PASTE THIS ENTIRE SCRIPT.
 * 3. Click 'Save' (floppy disk icon).
 * 4. Click 'Deploy' > 'Manage deployments' > Click the pencil icon (Edit) next to active deployment.
 * 5. Under 'Version', select 'New version'.
 * 6. Click 'Deploy'. That's it!
 */

// Global constant fallback keys
var SPREADSHEET_PROP_KEY = "MARGDARSH_SPREADSHEET_ID";
var DRIVE_FOLDER_NAME = "MargDarsh Payment Proofs";

/**
 * Ensures any phone number, UTR, ID, or symbol is stored strictly as PLAIN TEXT (TXT)
 * and NEVER executed as a formula or function in Google Sheets.
 */
function toPlainTextCell(val) {
  if (val === null || val === undefined) return "";
  var s = String(val).trim();
  if (s.length === 0) return "";
  
  // If already prefixed with single quote, preserve
  if (s.charAt(0) === "'") return s;
  
  // If it starts with formula triggers (+, =, -, @), prefix with ' so Sheets treats as pure text
  if (s.charAt(0) === '+' || s.charAt(0) === '=' || s.charAt(0) === '-' || s.charAt(0) === '@') {
    return "'" + s;
  }
  
  // If it is a phone number (e.g. 10+ digits or international), prefix with ' to avoid scientific notation
  if (/^(\+?\d[\d\s\-]{8,})$/.test(s)) {
    return "'" + s;
  }
  
  return s;
}

/**
 * One-click test function for the Apps Script Editor toolbar.
 */
function testRun() {
  Logger.log("=== STARTING MARGDARSH SELF-TEST ===");
  var ss = getOrInitSpreadsheet();
  if (!ss) {
    Logger.log("ERROR: Could not get or initialize Spreadsheet.");
    return "Failed to access spreadsheet";
  }
  ensureAllTabsExist(ss);
  Logger.log("✓ All 4 sheets initialized successfully with Plain Text format!");
  Logger.log("✓ Spreadsheet URL: " + ss.getUrl());

  var folder = getOrCreateDriveFolder();
  if (folder) {
    Logger.log("✓ Google Drive folder ready: " + folder.getUrl());
  }

  Logger.log("=== MARGDARSH BACKEND READY FOR PRODUCTION ===");
  return "All 4 sheets and Drive folder ready: " + ss.getUrl();
}

/**
 * Handles HTTP GET requests.
 */
function doGet(e) {
  try {
    var ss = getOrInitSpreadsheet();
    if (!ss) {
      return jsonResponse({
        status: "error",
        message: "Spreadsheet initialization failed. Please authorize script permissions."
      });
    }
    ensureAllTabsExist(ss);

    var action = (e && e.parameter && e.parameter.action) ? String(e.parameter.action).toUpperCase().trim() : "";
    var page = (e && e.parameter && e.parameter.page) ? String(e.parameter.page).toLowerCase().trim() : "";

    // Teacher Web Portal
    if (page === "teacher" || action === "TEACHER" || action === "TEACHER_PORTAL") {
      return renderTeacherWebPortal(ss);
    }

    // Portal data endpoints
    if (action === "GET_ALL_TESTS") {
      return handleGetAllTests(ss);
    }
    if (action === "GET_ALL_SUBMISSIONS") {
      return handleGetAllSubmissions(ss);
    }

    // 1. Live Admin Data fetch
    if (action === "GET_ADMIN_DATA" || action === "GET_ALL_DATA") {
      return handleGetAdminData(ss);
    }

    // 2. Coupon list fetch
    if (action === "GET_COUPONS") {
      return handleGetCoupons(ss);
    }

    // 3. One-click Cleanup & Deduplication tool
    if (action === "CLEAN_DUPLICATES" || action === "DEDUPLICATE") {
      return cleanAndDeduplicateAllSheets(ss);
    }

    // 4. Ping test
    if (action === "PING" || action === "TEST") {
      return jsonResponse({
        status: "success",
        message: "MargDarsh Enterprise 7-Sheet Server is ONLINE (Tests, Auth & Deduplication Active)",
        spreadsheetUrl: ss.getUrl()
      });
    }

    // 5. If opened in a web browser, render the Web Dashboard
    return renderWebDashboard(ss);

  } catch (err) {
    Logger.log("Error in doGet: " + err.toString());
    return jsonResponse({
      status: "error",
      message: err.toString()
    });
  }
}

/**
 * Handles HTTP POST requests from the Android Application.
 */
function doPost(e) {
  if (!e || !e.postData || !e.postData.contents) {
    return jsonResponse({
      status: "success",
      message: "MargDarsh Webhook endpoint is active and awaiting POST requests with action payloads."
    });
  }

  var lock = LockService.getScriptLock();
  try {
    lock.waitLock(30000);
  } catch (lockErr) {
    return jsonResponse({
      status: "error",
      message: "Server busy processing concurrent requests. Please retry in a few seconds."
    });
  }

  try {
    var raw = e.postData.contents;
    var data = {};
    try {
      data = JSON.parse(raw);
    } catch (parseErr) {
      return jsonResponse({ status: "error", message: "Invalid JSON payload" });
    }

    var ss = getOrInitSpreadsheet();
    if (!ss) {
      return jsonResponse({ status: "error", message: "Failed to access spreadsheet backend" });
    }
    ensureAllTabsExist(ss);

    var action = String(data.action || data.Stage || "").toUpperCase().trim();

    // 1. Connection Ping
    if (action === "PING" || action === "CONNECTION_TEST") {
      return jsonResponse({
        status: "success",
        message: "MargDarsh 4-Sheet Server is ACTIVE & operational on Google Sheets + Drive",
        spreadsheetUrl: ss.getUrl()
      });
    }

    // 2. Get Live Admin Data (Pulls all 4 sheets with deduplicated stats)
    if (action === "GET_ADMIN_DATA" || action === "GET_ALL_DATA") {
      return handleGetAdminData(ss);
    }

    // 3. Clean Duplicates in All Sheets
    if (action === "CLEAN_DUPLICATES" || action === "DEDUPLICATE") {
      return cleanAndDeduplicateAllSheets(ss);
    }

    // 4. Validate Coupon in Sheet 4
    if (action === "VALIDATE_COUPON" || action === "CHECK_COUPON") {
      return handleValidateCoupon(ss, data);
    }

    // 5. Create New Coupon in Sheet 4
    if (action === "CREATE_COUPON") {
      return handleCreateCoupon(ss, data);
    }

    // 6. Toggle Coupon in Sheet 4
    if (action === "TOGGLE_COUPON") {
      return handleToggleCoupon(ss, data);
    }

    // 7. Explicit Student Registration / Holistic Dossier Sync (Sheet 1) - 1 Student = 1 Row
    if (action === "UPSERT_STUDENT" || action === "REGISTRATION" || action === "STUDENT_REGISTRATION" || action === "SYNC_DOSSIER") {
      return handleStudentUpsert(ss, data);
    }

    // 8. Record Payment & Upload Proof Screenshot to Drive (Sheet 2) - DEDUPLICATED
    if (action === "RECORD_PAYMENT" || action === "SUBMIT_PAYMENT_SCREENSHOT") {
      return handleRecordPayment(ss, data);
    }

    // 9. 1-on-1 Counselling Booking (Sheet 3 + Sheet 2) - DEDUPLICATED
    if (action === "BOOK_COUNSELLING") {
      var payRes = handleRecordPayment(ss, data);
      recordInPaidCounsellingSheet(ss, data);
      return jsonResponse({
        status: "success",
        message: "Counselling booking logged in Sheet 3 and payment securely saved in Sheet 2 (Deduplicated)."
      });
    }

    // 10. Student Account Registration & Login (Sheet 6)
    if (action === "REGISTER_STUDENT" || action === "LOGIN_STUDENT") {
      return handleStudentAccountAuth(ss, data);
    }

    // 11. Create / Publish Academic MCQ Test (Sheet 5) - From Teacher Web Portal
    if (action === "CREATE_TEST" || action === "PUBLISH_TEST") {
      return handleCreateTest(ss, data);
    }

    // 12. Fetch Tests For Target Class (Sheet 5)
    if (action === "GET_CLASS_TESTS" || action === "GET_TESTS") {
      return handleGetClassTests(ss, data);
    }

    // 13. Submit Student Test & Calculate Points (Sheet 7 + Sheet 6)
    if (action === "SUBMIT_TEST") {
      return handleSubmitTest(ss, data);
    }

    // 14. Fetch Class Leaderboard (Sheet 6 + Sheet 7)
    if (action === "GET_LEADERBOARD" || action === "GET_CLASS_LEADERBOARD") {
      return handleGetLeaderboard(ss, data);
    }

    // 15. Fetch All Tests (Teacher Portal)
    if (action === "GET_ALL_TESTS") {
      return handleGetAllTests(ss);
    }

    // 16. Fetch All Submissions (Teacher Portal)
    if (action === "GET_ALL_SUBMISSIONS") {
      return handleGetAllSubmissions(ss);
    }

    // 17. Delete or Archive Test (Sheet 5)
    if (action === "DELETE_TEST" || action === "ARCHIVE_TEST") {
      return handleArchiveTest(ss, data);
    }

    // 18. Default fallback: Student Registration / Holistic Dossier Sync (Sheet 1) - 1 Student = 1 Row
    return handleStudentUpsert(ss, data);

  } catch (error) {
    Logger.log("Critical Error in doPost: " + error.toString());
    return jsonResponse({
      status: "error",
      message: error.toString()
    });
  } finally {
    try { lock.releaseLock(); } catch (_) {}
  }
}

/**
 * Resilient Spreadsheet Resolver
 */
function getOrInitSpreadsheet() {
  var ss = null;

  // 1. Bound script check
  try {
    ss = SpreadsheetApp.getActiveSpreadsheet();
    if (ss) return ss;
  } catch (_) {}

  // 2. Check saved ID
  try {
    var scriptProps = PropertiesService.getScriptProperties();
    var savedId = scriptProps.getProperty(SPREADSHEET_PROP_KEY);
    if (savedId) {
      ss = SpreadsheetApp.openById(savedId);
      if (ss) return ss;
    }
  } catch (_) {}

  // 3. Standalone mode auto-creation
  try {
    ss = SpreadsheetApp.create("MargDarsh Career Intelligence - Enterprise Database");
    PropertiesService.getScriptProperties().setProperty(SPREADSHEET_PROP_KEY, ss.getId());
    Logger.log("Auto-created MargDarsh Spreadsheet: " + ss.getUrl());
    return ss;
  } catch (e) {
    Logger.log("Unable to create spreadsheet: " + e.toString());
    return null;
  }
}

/**
 * Resilient Google Drive Folder Resolver
 */
function getOrCreateDriveFolder() {
  try {
    var folders = DriveApp.getFoldersByName(DRIVE_FOLDER_NAME);
    if (folders.hasNext()) {
      return folders.next();
    }
    var newFolder = DriveApp.createFolder(DRIVE_FOLDER_NAME);
    newFolder.setDescription("Auto-archived UPI payment receipts from MargDarsh Android Application");
    try {
      newFolder.setSharing(DriveApp.Access.ANYONE_WITH_LINK, DriveApp.Permission.VIEW);
    } catch (_) {}
    return newFolder;
  } catch (e) {
    Logger.log("Drive folder error: " + e.toString());
    return null;
  }
}

/**
 * AUTOMATICALLY ENSURES THE 4 EXACT SHEETS EXIST AND APPLIES PLAIN TEXT FORMATTING
 */
function ensureAllTabsExist(ss) {
  if (!ss) return;

  // Sheet 1: Student Registrations & Profiles
  var s1 = ss.getSheetByName("Student Registrations & Profiles") || ss.getSheetByName("Student Dossiers");
  if (!s1) {
    var sheets = ss.getSheets();
    if (sheets.length === 1 && sheets[0].getLastRow() <= 1) {
      sheets[0].setName("Student Registrations & Profiles");
      s1 = sheets[0];
    } else {
      s1 = ss.insertSheet("Student Registrations & Profiles", 0);
    }
  } else if (s1.getName() === "Student Dossiers") {
    s1.setName("Student Registrations & Profiles");
  }

  if (s1.getLastRow() === 0) {
    var h1 = [
      "Student Ref ID", "Last Activity Timestamp", "Student Name", "Parent Name",
      "WhatsApp Number", "Email Address", "Class / Grade", "School Name",
      "City / Location", "Assessment Stage", "Recommended Stream", "Top Careers",
      "Psychological Archetype", "Analytical %", "Spatial %", "Verbal %",
      "Enterprising %", "Social EQ %", "Creative %", "Practical %",
      "Payment / Dossier Status", "Payment UTR", "Screenshot Drive Link",
      "Answers to Student Questions", "Answers to Parent Questions"
    ];
    s1.appendRow(h1);
    s1.getRange(1, 1, 1, h1.length).setBackground("#0F2042").setFontColor("#FFFFFF").setFontWeight("bold");
    s1.setFrozenRows(1);
  }
  // Enforce Plain Text format on Student Ref ID (Col A), Phone (Col E), UTR (Col V)
  try {
    s1.getRange("A:A").setNumberFormat("@");
    s1.getRange("E:E").setNumberFormat("@");
    s1.getRange("V:V").setNumberFormat("@");
  } catch (_) {}

  // Sheet 2: Payment Records & Drive Proofs
  var s2 = ss.getSheetByName("Payment Records & Drive Proofs") || ss.getSheetByName("Payment Transactions");
  if (!s2) {
    s2 = ss.insertSheet("Payment Records & Drive Proofs", 1);
  } else if (s2.getName() === "Payment Transactions") {
    s2.setName("Payment Records & Drive Proofs");
  }

  if (s2.getLastRow() === 0) {
    var h2 = [
      "Timestamp (IST)", "Student Name", "Parent Name", "WhatsApp / Phone",
      "Email Address", "Student Ref ID", "Service Bought", "Net Revenue Collected (Amount Paid)",
      "Catalogue Price", "Discount Availed (Coupon Waived)", "Coupon Applied", "Payment UTR / Ref",
      "Screenshot Drive Link", "Payment Verification Status", "Notes"
    ];
    s2.appendRow(h2);
    s2.getRange(1, 1, 1, h2.length).setBackground("#1E3A8A").setFontColor("#FFFFFF").setFontWeight("bold");
    s2.setFrozenRows(1);
  }
  // Enforce Plain Text format on Phone (Col D), Ref ID (Col F), UTR (Col L) and Currency on H, I, J
  try {
    s2.getRange("D:D").setNumberFormat("@");
    s2.getRange("F:F").setNumberFormat("@");
    s2.getRange("L:L").setNumberFormat("@");
    s2.getRange("H:J").setNumberFormat('"₹"#,##0');
  } catch (_) {}

  // Sheet 3: 1-on-1 Counselling Students
  var s3 = ss.getSheetByName("1-on-1 Counselling Students") || ss.getSheetByName("Paid Counselling Students");
  if (!s3) {
    s3 = ss.insertSheet("1-on-1 Counselling Students", 2);
  } else if (s3.getName() === "Paid Counselling Students") {
    s3.setName("1-on-1 Counselling Students");
  }

  if (s3.getLastRow() === 0) {
    var h3 = [
      "Student Name", "Parent Name", "Phone Number", "Email ID", "Class / Grade",
      "School / Institute", "City", "Payment Status & Amount", "Coupon Applied",
      "Payment UTR / Ref", "Booking Timestamp (IST)", "Booking Status",
      "Recommended Stream", "Top Career Options", "Student Reference ID", "Dilemma / Session Notes",
      "Cloud Screenshot Proof (Google Drive / CDN)"
    ];
    s3.appendRow(h3);
    s3.getRange(1, 1, 1, h3.length).setBackground("#0A2647").setFontColor("#FFFFFF").setFontWeight("bold");
    s3.setFrozenRows(1);
  }
  // Enforce Plain Text format on Phone (Col C), UTR (Col J), Ref ID (Col O)
  try {
    s3.getRange("C:C").setNumberFormat("@");
    s3.getRange("J:J").setNumberFormat("@");
    s3.getRange("O:O").setNumberFormat("@");
  } catch (_) {}

  // Sheet 4: Coupons Creation & Usage
  var s4 = ss.getSheetByName("Coupons Creation & Usage") || ss.getSheetByName("Coupons");
  if (!s4) {
    s4 = ss.insertSheet("Coupons Creation & Usage", 3);
  } else if (s4.getName() === "Coupons") {
    s4.setName("Coupons Creation & Usage");
  }

  if (s4.getLastRow() === 0) {
    var h4 = [
      "Coupon Code", "Discount (%)", "Max Usage Limit", "Current Usage Count",
      "Eligible Services", "Active (TRUE/FALSE)", "Notes / Description"
    ];
    s4.appendRow(h4);
    s4.getRange(1, 1, 1, h4.length).setBackground("#064E3B").setFontColor("#FFFFFF").setFontWeight("bold");
    s4.setFrozenRows(1);

    var seedCoupons = [
      ["MARG50", 50, 100, 0, "ALL", true, "Standard 50% discount for all services"],
      ["FREE100", 100, 20, 0, "ALL", true, "100% Full Scholarship voucher (Limit: 20)"],
      ["SCHOLARSHIP100", 100, 15, 0, "DOSSIER,COUNSELLING", true, "Merit scholarship code"],
      ["SAVE50", 50, 50, 0, "ALL", true, "Special promo code"],
      ["RESOURCE50", 50, 100, 0, "RESOURCES", true, "50% off Handbook Bundle"]
    ];
    s4.getRange(2, 1, seedCoupons.length, 7).setValues(seedCoupons);
  }
  try {
    s4.getRange("A:A").setNumberFormat("@");
  } catch (_) {}

  // Sheet 5: Classwise Tests
  var s5 = ss.getSheetByName("Classwise Tests");
  if (!s5) {
    s5 = ss.insertSheet("Classwise Tests", 4);
  }
  if (s5.getLastRow() === 0) {
    var h5 = [
      "Test ID", "Target Class", "Subject", "Title", "Syllabus",
      "Start Time (ISO)", "End Time (ISO)", "Duration Mins", "Total Marks",
      "Questions JSON", "Status", "Created At"
    ];
    s5.appendRow(h5);
    s5.getRange(1, 1, 1, h5.length).setBackground("#1E40AF").setFontColor("#FFFFFF").setFontWeight("bold");
    s5.setFrozenRows(1);
  }
  try {
    s5.getRange("A:A").setNumberFormat("@");
    s5.getRange("B:B").setNumberFormat("@");
  } catch (_) {}

  // Sheet 6: Student Accounts
  var s6 = ss.getSheetByName("Student Accounts");
  if (!s6) {
    s6 = ss.insertSheet("Student Accounts", 5);
  }
  if (s6.getLastRow() === 0) {
    var h6 = [
      "Student ID", "Student Name", "Phone Number", "Email Address", "Class",
      "Password Hash", "Total Points", "Tests Taken", "Average Accuracy %", "Registered At"
    ];
    s6.appendRow(h6);
    s6.getRange(1, 1, 1, h6.length).setBackground("#047857").setFontColor("#FFFFFF").setFontWeight("bold");
    s6.setFrozenRows(1);
  }
  try {
    s6.getRange("A:A").setNumberFormat("@");
    s6.getRange("C:C").setNumberFormat("@");
  } catch (_) {}

  // Sheet 7: Test Submissions & Results
  var s7 = ss.getSheetByName("Test Submissions & Results");
  if (!s7) {
    s7 = ss.insertSheet("Test Submissions & Results", 6);
  }
  if (s7.getLastRow() === 0) {
    var h7 = [
      "Submission ID", "Test ID", "Student ID", "Student Name", "Class",
      "Score", "Total Marks", "Accuracy %", "Time Taken (Seconds)",
      "Points Awarded", "Speed Bonus", "Growth Bonus", "Answers JSON", "Submitted At"
    ];
    s7.appendRow(h7);
    s7.getRange(1, 1, 1, h7.length).setBackground("#B45309").setFontColor("#FFFFFF").setFontWeight("bold");
    s7.setFrozenRows(1);
  }
  try {
    s7.getRange("A:A").setNumberFormat("@");
    s7.getRange("B:B").setNumberFormat("@");
    s7.getRange("C:C").setNumberFormat("@");
  } catch (_) {}
}

/**
 * PULLS LIVE DATA FROM ALL 4 SHEETS FOR ADMIN PORTAL WITH CLEAN ACCURATE REVENUE MATH
 */
function handleGetAdminData(ss) {
  var s1 = ss.getSheetByName("Student Registrations & Profiles") || ss.getSheetByName("Student Dossiers");
  var s2 = ss.getSheetByName("Payment Records & Drive Proofs") || ss.getSheetByName("Payment Transactions");
  var s3 = ss.getSheetByName("1-on-1 Counselling Students") || ss.getSheetByName("Paid Counselling Students");
  var s4 = ss.getSheetByName("Coupons Creation & Usage") || ss.getSheetByName("Coupons");

  // 1. Students list (Sheet 1)
  var studentsList = [];
  if (s1 && s1.getLastRow() > 1) {
    var rows1 = s1.getRange(2, 1, s1.getLastRow() - 1, Math.min(s1.getLastColumn(), 25)).getValues();
    for (var i = 0; i < rows1.length; i++) {
      var r = rows1[i];
      studentsList.push({
        studentId: String(r[0] || "").replace(/^'/, ""),
        timestamp: String(r[1] || ""),
        studentName: String(r[2] || ""),
        parentName: String(r[3] || ""),
        phone: String(r[4] || "").replace(/^'/, ""),
        email: String(r[5] || ""),
        studentClass: String(r[6] || ""),
        school: String(r[7] || ""),
        city: String(r[8] || ""),
        lastStage: String(r[9] || ""),
        recommendedStream: String(r[10] || ""),
        topCareers: String(r[11] || ""),
        analyticalScore: String(r[13] || ""),
        creativeScore: String(r[18] || ""),
        paymentStatus: String(r[20] || "")
      });
    }
  }

  // 2. Payments list with Google Drive links (Sheet 2) - Deduplicated Net Revenue Calculation
  var paymentsList = [];
  var totalRevenue = 0.0;
  var totalDiscountGiven = 0.0;
  var totalGrossValue = 0.0;
  var seenPaymentKeys = {};

  if (s2 && s2.getLastRow() > 1) {
    var rows2 = s2.getRange(2, 1, s2.getLastRow() - 1, Math.min(s2.getLastColumn(), 15)).getValues();
    for (var j = 0; j < rows2.length; j++) {
      var p = rows2[j];
      var rawAmount = String(p[7] || "0").replace(/[^0-9.]/g, "");
      var amtNum = parseFloat(rawAmount) || 0.0;
      var rawOrig = String(p[8] || "0").replace(/[^0-9.]/g, "");
      var origNum = parseFloat(rawOrig) || amtNum;
      var rawDisc = String(p[9] || "0").replace(/[^0-9.]/g, "");
      var discNum = parseFloat(rawDisc) || 0.0;

      var pCoupon = String(p[10] || "").trim().toUpperCase();
      var pUtr = String(p[11] || "").trim().toUpperCase().replace(/^'/, "");

      // STRICT RULE: Exclude all coupon discounts from revenue collection!
      // Realized cash earned is strictly net cash (Original Price minus all Coupon Discounts).
      if (pUtr.indexOf("FREE100") !== -1 || pUtr.indexOf("100") !== -1 || pCoupon.indexOf("FREE100") !== -1 || pCoupon.indexOf("100") !== -1) {
        amtNum = 0.0;
        if (discNum <= 0 && origNum > 0) discNum = origNum;
      } else if (discNum > 0 && origNum > 0) {
        amtNum = Math.max(0, origNum - discNum);
      } else if (pCoupon && pCoupon !== "-" && pCoupon !== "NONE") {
        if (pCoupon.indexOf("50") !== -1) {
          discNum = Math.round(origNum * 0.5);
          amtNum = Math.max(0, origNum - discNum);
        }
      }

      var pStudent = String(p[5] || p[3] || "").trim().toUpperCase().replace(/^'/, "");
      var pService = String(p[6] || "").trim().toLowerCase();
      var dedupeKey = (pUtr && pUtr !== "PENDING_VERIFICATION") ? pUtr : (pStudent + "_" + pService);

      // Only count revenue for unique verified transactions
      if (!seenPaymentKeys[dedupeKey]) {
        totalRevenue += amtNum; // STRICTLY REALIZED NET CASH ONLY - DISCOUNTS NEVER COUNTED!
        totalDiscountGiven += discNum;
        totalGrossValue += origNum;
        seenPaymentKeys[dedupeKey] = true;
      }

      paymentsList.push({
        timestamp: String(p[0] || ""),
        studentName: String(p[1] || ""),
        parentName: String(p[2] || ""),
        phone: String(p[3] || "").replace(/^'/, ""),
        email: String(p[4] || ""),
        studentId: String(p[5] || "").replace(/^'/, ""),
        serviceBought: String(p[6] || ""),
        amountPaid: String(p[7] || "₹0"),
        originalPrice: String(p[8] || ""),
        discount: String(p[9] || ""),
        couponApplied: String(p[10] || ""),
        utrNumber: pUtr,
        driveScreenshotUrl: String(p[12] || ""),
        verificationStatus: String(p[13] || "Verified"),
        notes: String(p[14] || "")
      });
    }
  }

  // 3. Counselling Bookings (Sheet 3)
  var counsellingList = [];
  if (s3 && s3.getLastRow() > 1) {
    var rows3 = s3.getRange(2, 1, s3.getLastRow() - 1, Math.min(s3.getLastColumn(), 17)).getValues();
    for (var k = 0; k < rows3.length; k++) {
      var c = rows3[k];
      var cDriveUrl = String(c[16] || "").trim();
      var cUtr = String(c[9] || "").trim().toUpperCase().replace(/^'/, "");
      var cPhone = String(c[2] || "").replace(/[^0-9]/g, "").slice(-10);

      // If missing in Sheet 3, check matching paymentsList entry by UTR or phone
      if (!cDriveUrl.startsWith("http")) {
        for (var pIdx = 0; pIdx < paymentsList.length; pIdx++) {
          var pItem = paymentsList[pIdx];
          var pPhoneClean = String(pItem.phone || "").replace(/[^0-9]/g, "").slice(-10);
          if (pItem.driveScreenshotUrl && pItem.driveScreenshotUrl.startsWith("http")) {
            if ((cUtr && pItem.utrNumber && pItem.utrNumber.toUpperCase() === cUtr) ||
                (cPhone.length >= 10 && pPhoneClean.length >= 10 && cPhone === pPhoneClean)) {
              cDriveUrl = pItem.driveScreenshotUrl;
              break;
            }
          }
        }
      }

      counsellingList.push({
        studentName: String(c[0] || ""),
        parentName: String(c[1] || ""),
        phone: String(c[2] || "").replace(/^'/, ""),
        email: String(c[3] || ""),
        studentClass: String(c[4] || ""),
        school: String(c[5] || ""),
        city: String(c[6] || ""),
        amountPaid: String(c[7] || ""),
        couponApplied: String(c[8] || ""),
        utrNumber: cUtr,
        timestamp: String(c[10] || ""),
        sessionStatus: String(c[11] || "Scheduled"),
        recommendedStream: String(c[12] || ""),
        topCareers: String(c[13] || ""),
        studentId: String(c[14] || "").replace(/^'/, ""),
        dilemma: String(c[15] || ""),
        driveScreenshotUrl: cDriveUrl
      });
    }
  }

  // 4. Coupons (Sheet 4)
  var couponsList = [];
  var activeCouponsCount = 0;
  if (s4 && s4.getLastRow() > 1) {
    var rows4 = s4.getRange(2, 1, s4.getLastRow() - 1, Math.min(s4.getLastColumn(), 7)).getValues();
    for (var m = 0; m < rows4.length; m++) {
      var cRow = rows4[m];
      var code = String(cRow[0] || "").trim().toUpperCase().replace(/^'/, "");
      var disc = Number(cRow[1]) || 0;
      var maxL = Number(cRow[2]) || 0;
      var curU = Number(cRow[3]) || 0;
      var rem = maxL > 0 ? Math.max(0, maxL - curU) : 9999;
      var act = cRow[5] === true || String(cRow[5]).toUpperCase() === "TRUE" || String(cRow[5]).toUpperCase() === "ACTIVE";
      if (act) activeCouponsCount++;

      couponsList.push({
        code: code,
        discountPercent: disc,
        maxLimit: maxL,
        currentUsage: curU,
        remainingUses: rem,
        service: String(cRow[4] || "ALL"),
        isActive: act,
        description: String(cRow[6] || "")
      });
    }
  }

  return jsonResponse({
    status: "success",
    summary: {
      totalRevenue: totalRevenue,
      totalDiscountGiven: totalDiscountGiven,
      totalGrossValue: totalGrossValue,
      totalStudents: studentsList.length,
      totalPayments: paymentsList.length,
      totalCounsellingBookings: counsellingList.length,
      activeCouponsCount: activeCouponsCount
    },
    students: studentsList,
    payments: paymentsList,
    counselling: counsellingList,
    coupons: couponsList,
    spreadsheetUrl: ss.getUrl(),
    timestamp: new Date().toLocaleString("en-IN", { timeZone: "Asia/Kolkata" })
  });
}

/**
 * Validates a coupon against Sheet 4
 */
function handleValidateCoupon(ss, data) {
  var couponSheet = ss.getSheetByName("Coupons Creation & Usage") || ss.getSheetByName("Coupons");
  var rawCode = String(data.coupon_code || data.code || "").trim().toUpperCase();
  var inputCode = rawCode.replace(/^COUPON_/, "").trim().toUpperCase();
  if (inputCode.indexOf("_") !== -1 && inputCode.split("_").length >= 3) {
    inputCode = inputCode.split("_")[0];
  }
  var service = String(data.service || "ALL").trim().toUpperCase();

  if (!inputCode) {
    return jsonResponse({ status: "error", message: "Please enter a coupon code." });
  }

  // Ensure Sheet 4 usage counts are 100% synchronized with actual Sheet 2 payments
  try { reconcileCouponUsageCounts(ss); } catch (_) {}

  var lastRow = couponSheet.getLastRow();
  if (lastRow <= 1) {
    return jsonResponse({ status: "error", message: "No coupons available." });
  }

  var values = couponSheet.getRange(2, 1, lastRow - 1, 7).getValues();
  for (var i = 0; i < values.length; i++) {
    var rowCode = String(values[i][0] || "").trim().toUpperCase().replace(/^'/, "");
    if (rowCode === inputCode) {
      var discountPercent = parseFloat(String(values[i][1] || "0").replace(/[^0-9.]/g, "")) || 0;
      var maxLimit = Number(values[i][2]) || 0;
      var currentUsage = Number(values[i][3]) || 0;
      var eligibility = String(values[i][4] || "ALL").trim().toUpperCase();
      var isActive = values[i][5] === true || String(values[i][5]).toUpperCase() === "TRUE" || String(values[i][5]).toUpperCase() === "ACTIVE";

      if (!isActive) {
        return jsonResponse({
          status: "error",
          message: "Coupon '" + inputCode + "' is currently inactive or expired."
        });
      }

      if (eligibility !== "ALL" && eligibility.indexOf(service) === -1 && service !== "ALL") {
        return jsonResponse({
          status: "error",
          message: "Coupon '" + inputCode + "' is not applicable for " + service + "."
        });
      }

      if (maxLimit > 0 && currentUsage >= maxLimit) {
        return jsonResponse({
          status: "error",
          message: "Coupon limit reached (" + currentUsage + "/" + maxLimit + " used). No redemptions remaining."
        });
      }

      var remaining = maxLimit > 0 ? (maxLimit - currentUsage) : 9999;
      return jsonResponse({
        status: "success",
        coupon_code: inputCode,
        discount_percent: discountPercent,
        max_limit: maxLimit,
        current_usage: currentUsage,
        remaining_uses: remaining,
        message: "✓ Coupon '" + inputCode + "' Applied! " + discountPercent + "% Discount (" + remaining + " uses left)"
      });
    }
  }

  return jsonResponse({
    status: "error",
    message: "Invalid coupon code '" + inputCode + "'. Please check the spelling."
  });
}

/**
 * Creates or updates a coupon in Sheet 4
 */
function handleCreateCoupon(ss, data) {
  var sheet = ss.getSheetByName("Coupons Creation & Usage") || ss.getSheetByName("Coupons");
  var code = String(data.code || data.coupon_code || "").trim().toUpperCase();
  var discount = Number(data.discount || data.discount_percent) || 50;
  var maxLimit = Number(data.max_limit || data.limit) || 100;
  var service = String(data.service || "ALL").trim().toUpperCase();
  var notes = String(data.notes || data.description || "Created via MargDarsh Admin Portal").trim();

  if (!code) {
    return jsonResponse({ status: "error", message: "Coupon code cannot be empty" });
  }

  var lastRow = sheet.getLastRow();
  var foundRow = -1;
  if (lastRow > 1) {
    var existingCodes = sheet.getRange(2, 1, lastRow - 1, 1).getValues();
    for (var i = 0; i < existingCodes.length; i++) {
      if (String(existingCodes[i][0] || "").trim().toUpperCase().replace(/^'/, "") === code) {
        foundRow = i + 2;
        break;
      }
    }
  }

  if (foundRow > 0) {
    sheet.getRange(foundRow, 2).setValue(discount);
    sheet.getRange(foundRow, 3).setValue(maxLimit);
    sheet.getRange(foundRow, 5).setValue(service);
    sheet.getRange(foundRow, 6).setValue(true);
    sheet.getRange(foundRow, 7).setValue(notes);
  } else {
    sheet.appendRow([toPlainTextCell(code), discount, maxLimit, 0, service, true, notes]);
  }

  return jsonResponse({
    status: "success",
    message: "Coupon '" + code + "' (" + discount + "% off, limit " + maxLimit + ") saved in Sheet 4."
  });
}

/**
 * Toggles coupon active status in Sheet 4
 */
function handleToggleCoupon(ss, data) {
  var sheet = ss.getSheetByName("Coupons Creation & Usage") || ss.getSheetByName("Coupons");
  var code = String(data.code || data.coupon_code || "").trim().toUpperCase();
  var setActive = data.active === true || String(data.active).toUpperCase() === "TRUE";

  var lastRow = sheet.getLastRow();
  if (lastRow > 1) {
    var existingCodes = sheet.getRange(2, 1, lastRow - 1, 1).getValues();
    for (var i = 0; i < existingCodes.length; i++) {
      if (String(existingCodes[i][0] || "").trim().toUpperCase().replace(/^'/, "") === code) {
        sheet.getRange(i + 2, 6).setValue(setActive);
        return jsonResponse({
          status: "success",
          message: "Coupon '" + code + "' status updated to " + (setActive ? "ACTIVE" : "DISABLED")
        });
      }
    }
  }
  return jsonResponse({ status: "error", message: "Coupon code not found in Sheet 4" });
}

/**
 * Uploads screenshot to Google Drive, logs row in Sheet 2 with STRICT DEDUPLICATION.
 * Single students will NEVER be recorded twice for the same payment transaction.
 */
function handleRecordPayment(ss, data) {
  var txnSheet = ss.getSheetByName("Payment Records & Drive Proofs") || ss.getSheetByName("Payment Transactions");
  var timestamp = String(data.timestamp || new Date().toLocaleString("en-IN", { timeZone: "Asia/Kolkata" }));
  var studentId = String(data.student_id || data.ref_id || "").trim();
  var studentName = String(data.student_name || data.name || "Student").trim();
  var parentName = String(data.parent_name || "").trim();
  var phone = String(data.phone || data.whatsapp_number || "").trim();
  var cleanPhone = phone.replace(/[^0-9]/g, "");
  if (cleanPhone.length > 10) cleanPhone = cleanPhone.slice(-10);

  var email = String(data.email || "").trim();
  var service = String(data.service || data.service_name || "MargDarsh Service").trim();
  var rawAmount = String(data.amount || data.amount_paid || "0").trim();
  var rawCoupon = String(data.coupon_code || data.coupon || data["Coupon Code"] || data["Coupon Applied"] || data.Coupon || "").trim();
  var utr = String(data.utr || data.utr_number || data.Payment_UTR || data["Payment UTR"] || "PENDING_VERIFICATION").trim();
  var notes = String(data.notes || "").trim();

  // Extract clean coupon code (strips 'COUPON_' prefix and any unique random suffix)
  var cleanCoupon = "";
  if (rawCoupon && rawCoupon !== "NONE" && rawCoupon !== "NULL" && rawCoupon !== "-" && rawCoupon !== "None" && rawCoupon.indexOf("None (Full Fee)") === -1) {
    var cpnCandidate = rawCoupon.replace(/^COUPON_/, "").trim().toUpperCase();
    if (cpnCandidate.indexOf("_") !== -1 && cpnCandidate.split("_").length >= 3) {
      cpnCandidate = cpnCandidate.split("_")[0];
    }
    cleanCoupon = cpnCandidate;
  }
  if (!cleanCoupon && utr.indexOf("COUPON_") !== -1) {
    cleanCoupon = utr.replace(/^COUPON_/, "").split("_")[0].trim().toUpperCase();
  }

  // Deduce standard catalogue price if needed
  var origPriceNum = parseFloat(String(data.original_price || "0").replace(/[^0-9.]/g, "")) || 0;
  var paidAmtNum = parseFloat(rawAmount.replace(/[^0-9.]/g, "")) || 0;
  if (origPriceNum <= 0) {
    if (service.toLowerCase().indexOf("counselling") !== -1 || (data.action && data.action === "BOOK_COUNSELLING")) origPriceNum = 999;
    else if (service.toLowerCase().indexOf("dossier") !== -1) origPriceNum = 499;
    else if (service.toLowerCase().indexOf("bundle") !== -1) origPriceNum = 70;
    else origPriceNum = paidAmtNum > 0 ? paidAmtNum : 18;
  }

  // Look up coupon discount in Sheet 4 if coupon code provided
  var couponDiscountPercent = 0;
  if (cleanCoupon) {
    try {
      var coupSheet = ss.getSheetByName("Coupons Creation & Usage") || ss.getSheetByName("Coupons");
      if (coupSheet && coupSheet.getLastRow() > 1) {
        var cData = coupSheet.getRange(2, 1, coupSheet.getLastRow() - 1, 6).getValues();
        for (var ci = 0; ci < cData.length; ci++) {
          var cCode = String(cData[ci][0] || "").trim().toUpperCase().replace(/^'/, "");
          if (cCode === cleanCoupon) {
            couponDiscountPercent = parseFloat(String(cData[ci][1] || "0").replace(/[^0-9.]/g, "")) || 0;
            break;
          }
        }
      }
    } catch (_) {}
  }

  // Fallback for known coupon percentages
  if (couponDiscountPercent === 0 && cleanCoupon) {
    if (cleanCoupon.indexOf("100") !== -1 || cleanCoupon === "FREE100" || cleanCoupon === "SCHOLARSHIP100") couponDiscountPercent = 100;
    else if (cleanCoupon.indexOf("50") !== -1 || cleanCoupon === "MARG50" || cleanCoupon === "SAVE50" || cleanCoupon === "RESOURCE50") couponDiscountPercent = 50;
  }

  // Calculate actual cash collected vs coupon discount (discounts are strictly excluded from revenue)
  var isPercent = String(data.discount || "").indexOf("%") !== -1;
  var rawPassedDisc = parseFloat(String(data.discount || "0").replace(/[^0-9.]/g, "")) || 0;
  var discNum = 0;

  if (couponDiscountPercent >= 100 || cleanCoupon.indexOf("100") !== -1 || utr.indexOf("100") !== -1 || paidAmtNum === 0 || (isPercent && rawPassedDisc >= 100)) {
    paidAmtNum = 0;
    discNum = origPriceNum;
    if (!cleanCoupon && utr.indexOf("COUPON_") !== -1) {
      cleanCoupon = utr.replace(/^COUPON_/, "").split("_")[0].trim().toUpperCase();
    }
  } else if (couponDiscountPercent > 0) {
    discNum = Math.round(origPriceNum * (couponDiscountPercent / 100));
    paidAmtNum = Math.max(0, origPriceNum - discNum);
  } else if (isPercent && rawPassedDisc > 0) {
    discNum = Math.round(origPriceNum * (rawPassedDisc / 100));
    paidAmtNum = Math.max(0, origPriceNum - discNum);
  } else if (cleanCoupon.indexOf("50") !== -1 || utr.indexOf("50") !== -1) {
    discNum = Math.round(origPriceNum * 0.5);
    paidAmtNum = Math.max(0, origPriceNum - discNum);
  } else if (rawPassedDisc > 0) {
    discNum = rawPassedDisc;
    paidAmtNum = Math.max(0, origPriceNum - discNum);
  } else {
    discNum = Math.max(0, origPriceNum - paidAmtNum);
  }

  // STRICT REVENUE INTEGRITY: Coupon discount is permanently excluded from revenue collection!
  paidAmtNum = Math.max(0, origPriceNum - discNum);

  var amountStr = String(Math.round(paidAmtNum));
  var originalPriceStr = String(Math.round(origPriceNum));
  var discountStr = String(Math.round(discNum));

  // 1. Upload screenshot to Google Drive if image base64 provided
  var driveUrl = "No Screenshot Attached";
  if (data.image_base64 && String(data.image_base64).trim().length > 100) {
    driveUrl = saveScreenshotToDrive(data.image_base64, data.image_mime, service, studentName, studentId);
  } else if (data.screenshot_url && String(data.screenshot_url).startsWith("http")) {
    driveUrl = String(data.screenshot_url);
  } else if (data.drive_screenshot_url && String(data.drive_screenshot_url).startsWith("http")) {
    driveUrl = String(data.drive_screenshot_url);
  }

  // 2. CHECK FOR EXISTING TRANSACTION IN SHEET 2 FOR THIS EXACT STUDENT ONLY
  var lastRow = txnSheet.getLastRow();
  var existingTxnRowIdx = -1;

  if (lastRow > 1) {
    var existingTxns = txnSheet.getRange(2, 1, lastRow - 1, Math.min(txnSheet.getLastColumn(), 15)).getValues();
    for (var t = 0; t < existingTxns.length; t++) {
      var row = existingTxns[t];
      var rowPhone = String(row[3] || "").replace(/[^0-9]/g, "").slice(-10);
      var rowStudentId = String(row[5] || "").trim().toUpperCase().replace(/^'/, "");
      var rowService = String(row[6] || "").trim().toLowerCase();
      var rowUtr = String(row[11] || "").trim().toUpperCase().replace(/^'/, "");

      // STRICT RULE: If different phone/student, it is NEVER a duplicate!
      var isSameStudent = (cleanPhone.length >= 10 && rowPhone.length >= 10 && cleanPhone === rowPhone) ||
                          (studentId && rowStudentId && rowStudentId === studentId.toUpperCase());
      if (!isSameStudent) continue;

      var isSameService = (service && rowService && (rowService.indexOf(service.toLowerCase()) !== -1 || service.toLowerCase().indexOf(rowService) !== -1));
      var isRealBankUtrMatch = (utr && utr.length >= 8 && utr.indexOf("COUPON_") === -1 && utr.indexOf("SCREENSHOT_") === -1 && utr.indexOf("WHATSAPP_") === -1 && rowUtr === utr.toUpperCase());

      // If same student and exact bank UTR, or same student and re-submitting same service:
      if (isRealBankUtrMatch || (isSameService && (utr === rowUtr || utr.indexOf("COUPON_") !== -1 || rowUtr.indexOf("COUPON_") !== -1 || rowUtr === "PENDING_VERIFICATION"))) {
        existingTxnRowIdx = t + 2;
        break;
      }
    }
  }

  if (existingTxnRowIdx > 0) {
    // TRANSACTION ALREADY EXISTS FOR THIS STUDENT: Update existing row in place with full revenue and coupon details
    var existingCouponInRow = String(txnSheet.getRange(existingTxnRowIdx, 11).getValue() || "").trim().toUpperCase().replace(/^'/, "");
    var finalCoupon = cleanCoupon || (existingCouponInRow !== "-" && existingCouponInRow !== "NONE" && existingCouponInRow !== "NULL" ? existingCouponInRow : "");
    var prevDiscInRow = String(txnSheet.getRange(existingTxnRowIdx, 10).getValue() || "").trim();
    var finalDiscStr = discNum > 0 ? ("₹" + discountStr) : (finalCoupon && origPriceNum > 0 && paidAmtNum === 0 ? ("₹" + originalPriceStr) : (prevDiscInRow !== "-" ? prevDiscInRow : "-"));

    txnSheet.getRange(existingTxnRowIdx, 8).setValue("₹" + amountStr);
    txnSheet.getRange(existingTxnRowIdx, 9).setValue("₹" + originalPriceStr);
    txnSheet.getRange(existingTxnRowIdx, 10).setValue(finalDiscStr);
    txnSheet.getRange(existingTxnRowIdx, 11).setValue(finalCoupon ? toPlainTextCell(finalCoupon) : (existingCouponInRow && existingCouponInRow !== "-" ? toPlainTextCell(existingCouponInRow) : "-"));
    if (utr && utr !== "PENDING_VERIFICATION") {
      txnSheet.getRange(existingTxnRowIdx, 12).setValue(toPlainTextCell(utr));
    }
    if (driveUrl !== "No Screenshot Attached") {
      txnSheet.getRange(existingTxnRowIdx, 13).setValue(driveUrl);
    }
    txnSheet.getRange(existingTxnRowIdx, 14).setValue((paidAmtNum === 0 || (finalCoupon && discNum >= origPriceNum)) ? "Verified (100% Scholarship)" : "Verified Paid");
    if (notes) {
      txnSheet.getRange(existingTxnRowIdx, 15).setValue(notes);
    }

    // Increment coupon usage ONLY IF this transaction had not yet recorded this coupon
    if (finalCoupon && existingCouponInRow !== finalCoupon) {
      incrementCouponUsage(ss, finalCoupon);
    }
    try { reconcileCouponUsageCounts(ss); } catch (_) {}

    Logger.log("Updated existing payment row " + existingTxnRowIdx + " for " + studentName + " (" + service + ") with coupon " + finalCoupon);

    // Update student status in Sheet 1
    try {
      data.payment_status = "Paid (" + service + " - ₹" + amountStr + (finalCoupon ? (", Coupon: " + finalCoupon) : "") + ")";
      data.utr_number = utr;
      data.screenshot_url = driveUrl;
      handleStudentUpsert(ss, data);
    } catch (_) {}

    // If service is Counselling, ensure Sheet 3 ('1-on-1 Counselling Students') is recorded!
    if (service.toLowerCase().indexOf("counselling") !== -1 || (data.action && data.action === "BOOK_COUNSELLING")) {
      try {
        data.coupon_code = finalCoupon || data.coupon_code;
        data.amount = amountStr;
        recordInPaidCounsellingSheet(ss, data);
      } catch (cErr) {
        Logger.log("Non-fatal: could not record counselling sheet: " + cErr);
      }
    }

    return jsonResponse({
      status: "success",
      deduplicated: true,
      row_index: existingTxnRowIdx,
      service: service,
      student_name: studentName,
      amount: amountStr,
      coupon: finalCoupon,
      drive_screenshot_url: driveUrl,
      message: "Payment transaction record updated with coupon " + finalCoupon + " in Sheet 2."
    });
  }

  // 3. Increment coupon usage count in Sheet 4 whenever a coupon is redeemed
  if (cleanCoupon) {
    incrementCouponUsage(ss, cleanCoupon);
  }
  try { reconcileCouponUsageCounts(ss); } catch (_) {}

  // 4. Append to Sheet 2 with PLAIN TEXT SANITIZATION
  var txnRow = [
    timestamp,
    studentName,
    parentName,
    toPlainTextCell(phone),
    email,
    toPlainTextCell(studentId),
    service,
    "₹" + amountStr,
    "₹" + originalPriceStr,
    discNum > 0 ? ("₹" + discountStr) : (cleanCoupon && origPriceNum > 0 && paidAmtNum === 0 ? ("₹" + originalPriceStr) : "-"),
    cleanCoupon ? toPlainTextCell(cleanCoupon) : "-",
    toPlainTextCell(utr),
    driveUrl,
    (paidAmtNum === 0 || (cleanCoupon && discNum >= origPriceNum)) ? "Verified (100% Scholarship)" : "Verified Paid",
    notes
  ];

  txnSheet.appendRow(txnRow);
  var newRowIdx = txnSheet.getLastRow();
  txnSheet.getRange(newRowIdx, 14).setBackground("#D1E7DD").setFontColor("#0F5132").setFontWeight("bold");

  // 5. Also update student status in Sheet 1: 'Student Registrations & Profiles'
  try {
    data.payment_status = "Paid (" + service + " - ₹" + amountStr + (cleanCoupon ? (", Coupon: " + cleanCoupon) : "") + ")";
    data.utr_number = utr;
    data.screenshot_url = driveUrl;
    handleStudentUpsert(ss, data);
  } catch (upErr) {
    Logger.log("Non-fatal: could not update student profile during payment log: " + upErr);
  }

  // 6. If service is Counselling, ensure Sheet 3 ('1-on-1 Counselling Students') is recorded!
  if (service.toLowerCase().indexOf("counselling") !== -1 || (data.action && data.action === "BOOK_COUNSELLING")) {
    try {
      data.coupon_code = cleanCoupon || data.coupon_code;
      data.amount = amountStr;
      recordInPaidCounsellingSheet(ss, data);
    } catch (cErr) {
      Logger.log("Non-fatal: could not record counselling sheet: " + cErr);
    }
  }

  return jsonResponse({
    status: "success",
    transaction_id: "TXN_" + new Date().getTime(),
    service: service,
    student_name: studentName,
    amount: amountStr,
    coupon: cleanCoupon,
    drive_screenshot_url: driveUrl,
    message: "Payment successfully recorded in Sheet 2 and screenshot archived in Google Drive."
  });
}

/**
 * Increments the coupon's Current Usage Count in Sheet 4
 */
function incrementCouponUsage(ss, couponCode) {
  try {
    if (!couponCode) return;
    var cleanCode = String(couponCode).replace(/^COUPON_/, "").split("_")[0].trim().toUpperCase();
    var couponSheet = ss.getSheetByName("Coupons Creation & Usage") || ss.getSheetByName("Coupons");
    if (!couponSheet) return;
    var lastRow = couponSheet.getLastRow();
    if (lastRow <= 1) return;

    var codes = couponSheet.getRange(2, 1, lastRow - 1, 1).getValues();
    for (var i = 0; i < codes.length; i++) {
      if (String(codes[i][0] || "").trim().toUpperCase().replace(/^'/, "") === cleanCode) {
        var countCell = couponSheet.getRange(i + 2, 4);
        var current = Number(countCell.getValue()) || 0;
        countCell.setValue(current + 1);
        Logger.log("Incremented coupon " + cleanCode + " usage count to " + (current + 1));
        break;
      }
    }
  } catch (err) {
    Logger.log("Error incrementing coupon: " + err);
  }
}

/**
 * Reconciles Sheet 4 coupon usage counts with actual Sheet 2 payment entries
 */
function reconcileCouponUsageCounts(ss) {
  try {
    var couponSheet = ss.getSheetByName("Coupons Creation & Usage") || ss.getSheetByName("Coupons");
    var txnSheet = ss.getSheetByName("Payment Records & Drive Proofs") || ss.getSheetByName("Payment Transactions");
    if (!couponSheet || couponSheet.getLastRow() <= 1) return;

    var actualCounts = {};
    if (txnSheet && txnSheet.getLastRow() > 1) {
      var txnData = txnSheet.getRange(2, 1, txnSheet.getLastRow() - 1, Math.min(txnSheet.getLastColumn(), 15)).getValues();
      for (var r = 0; r < txnData.length; r++) {
        var rowCpn = String(txnData[r][10] || "").trim().toUpperCase().replace(/^'/, "");
        var rowUtr = String(txnData[r][11] || "").trim().toUpperCase().replace(/^'/, "");
        var code = "";
        if (rowCpn && rowCpn !== "-" && rowCpn !== "NONE" && rowCpn !== "NULL" && rowCpn.indexOf("NONE") === -1) {
          code = rowCpn.replace(/^COUPON_/, "").split("_")[0].trim();
        } else if (rowUtr.indexOf("COUPON_") !== -1) {
          code = rowUtr.replace(/^COUPON_/, "").split("_")[0].trim();
        }
        if (code) {
          actualCounts[code] = (actualCounts[code] || 0) + 1;
        }
      }
    }

    var couponLastRow = couponSheet.getLastRow();
    var couponCodes = couponSheet.getRange(2, 1, couponLastRow - 1, 1).getValues();
    for (var i = 0; i < couponCodes.length; i++) {
      var cCode = String(couponCodes[i][0] || "").trim().toUpperCase().replace(/^'/, "");
      var count = actualCounts[cCode] || 0;
      couponSheet.getRange(i + 2, 4).setValue(count);
    }
  } catch (err) {
    Logger.log("Error reconciling coupon usage counts: " + err);
  }
}

/**
 * Saves base64 image data into Google Drive folder 'MargDarsh Payment Proofs'
 */
function saveScreenshotToDrive(base64Data, mimeType, service, studentName, studentId) {
  try {
    var targetFolder = getOrCreateDriveFolder();
    if (!targetFolder) {
      return "Drive Folder Unavailable";
    }

    var cleanService = service.replace(/[^a-zA-Z0-9]/g, "_").slice(0, 20);
    var cleanStudent = studentName.replace(/[^a-zA-Z0-9]/g, "_").slice(0, 25);
    var dateStamp = Utilities.formatDate(new Date(), "Asia/Kolkata", "yyyyMMdd_HHmmss");
    var fileName = "RECEIPT_" + cleanService + "_" + cleanStudent + "_" + dateStamp + ".jpg";

    var decodedBytes = Utilities.base64Decode(base64Data);
    var blob = Utilities.newBlob(decodedBytes, mimeType || "image/jpeg", fileName);

    var file = targetFolder.createFile(blob);
    try {
      file.setSharing(DriveApp.Access.ANYONE_WITH_LINK, DriveApp.Permission.VIEW);
    } catch (_) {}

    var fileId = file.getId();
    var fileUrl = "https://drive.google.com/file/d/" + fileId + "/view?usp=sharing";
    Logger.log("Saved payment screenshot to Drive: " + fileUrl + " (ID: " + fileId + ")");
    return fileUrl;
  } catch (e) {
    Logger.log("Error saving screenshot to Drive: " + e.toString());
    return "Drive Upload Failed: " + e.toString().slice(0, 80);
  }
}

/**
 * Sheet 1: Student Registrations & Profiles (1 Student = 1 Row Strict Upsert Engine)
 * Automatically writes Phone and ID as Plain Text to eliminate formula errors (#ERROR! / #NAME?).
 */
function handleStudentUpsert(ss, data) {
  var sheet = ss.getSheetByName("Student Registrations & Profiles") || ss.getSheetByName("Student Dossiers") || ss.getActiveSheet();

  var studentId = String(data.student_id || data.ref_id || data.Reference_ID || data["Reference ID"] || "").trim().replace(/^'/, "");
  var phone = String(data.phone || data.WhatsApp_Number || data.Number || data["WhatsApp Number"] || "").trim().replace(/^'/, "");
  var cleanPhone = phone.replace(/[^0-9]/g, "");
  if (cleanPhone.length > 10) cleanPhone = cleanPhone.slice(-10);

  var studentName = String(data.student_name || data.Student_Name || data["Student Name"] || "").trim();
  var parentName = String(data.parent_name || data.Parent_Name || data["Parent Name"] || "").trim();
  var email = String(data.email || data.Email || data.Gmail || "").trim();
  var studentClass = String(data.student_class || data.Class || "").trim();
  var school = String(data.school || data.School || "").trim();
  var city = String(data.city || data.City || "").trim();
  var stage = String(data.stage || data.Stage || "Registered").trim();
  var bestStream = String(data.best_stream || data.Recommended_Stream || data["Best stream"] || data["Recommended Stream"] || "").trim();
  var topCareers = String(data.top_careers || data.Top_Careers || data["Top job options"] || data["Top Careers"] || "").trim();
  var archetype = String(data.cognitive_archetype || data.Cognitive_Archetype || data["Cognitive Archetype"] || "").trim();
  var timestamp = String(data.last_updated || data.Times || data.Timestamp || new Date().toLocaleString("en-IN", { timeZone: "Asia/Kolkata" })).trim();
  var paymentStatus = String(data.payment_status || data.Payment_Status || data["Payment Status"] || "Free Assessment").trim();
  var paymentUtr = String(data.utr_number || data.Payment_UTR || data["Payment UTR"] || "").trim();
  var screenshotUrl = String(data.screenshot_url || "").trim();
  var studentAnswers = String(data.all_student_answers || data.Student_Answers || data["Answers to student questions"] || "").trim();
  var parentAnswers = String(data.all_parent_answers || data.Parent_Answers || data["Answers to parent questions"] || "").trim();

  var traitAnalytical = String(data.trait_analytical || "").trim();
  var traitSpatial = String(data.trait_spatial || "").trim();
  var traitVerbal = String(data.trait_verbal || "").trim();
  var traitEnterprising = String(data.trait_enterprising || "").trim();
  var traitSocial = String(data.trait_social || "").trim();
  var traitCreative = String(data.trait_creative || "").trim();
  var traitPractical = String(data.trait_practical || "").trim();

  // Pure plain text formatted map
  var fieldMap = {
    "student ref id": toPlainTextCell(studentId),
    "last activity timestamp": timestamp,
    "student name": studentName,
    "parent name": parentName,
    "whatsapp number": toPlainTextCell(phone),
    "phone": toPlainTextCell(phone),
    "email address": email,
    "email": email,
    "class / grade": studentClass,
    "school name": school,
    "city / location": city,
    "assessment stage": stage,
    "recommended stream": bestStream,
    "top careers": topCareers,
    "psychological archetype": archetype,
    "analytical %": traitAnalytical,
    "spatial %": traitSpatial,
    "verbal %": traitVerbal,
    "enterprising %": traitEnterprising,
    "social eq %": traitSocial,
    "creative %": traitCreative,
    "practical %": traitPractical,
    "payment / dossier status": paymentStatus,
    "payment utr": toPlainTextCell(paymentUtr),
    "screenshot drive link": screenshotUrl,
    "answers to student questions": studentAnswers,
    "answers to parent questions": parentAnswers
  };

  var lastCol = Math.max(sheet.getLastColumn(), 1);
  var headerRow = sheet.getRange(1, 1, 1, lastCol).getValues()[0];
  var normalizedHeaders = headerRow.map(function(h) { return String(h || "").trim().toLowerCase(); });

  var lastRow = sheet.getLastRow();
  var existingRowIdx = -1;

  if (lastRow >= 2) {
    var allRows = sheet.getRange(2, 1, lastRow - 1, lastCol).getValues();
    var idColIdx = -1;
    var phoneColIdx = -1;
    var nameColIdx = -1;

    for (var c = 0; c < normalizedHeaders.length; c++) {
      var h = normalizedHeaders[c];
      if (h === "student ref id" || h === "ref id" || h === "student_id" || h === "reference id") {
        idColIdx = c;
      } else if (idColIdx === -1 && (h.indexOf("student ref") !== -1 || h.indexOf("ref id") !== -1 || h === "id")) {
        idColIdx = c;
      }
      if (h === "whatsapp number" || h === "phone" || h === "phone number" || h === "mobile") {
        phoneColIdx = c;
      } else if (phoneColIdx === -1 && (h.indexOf("phone") !== -1 || h.indexOf("whatsapp") !== -1)) {
        phoneColIdx = c;
      }
      if (h === "student name" || h === "student_name" || h === "name") {
        nameColIdx = c;
      }
    }

    for (var r = 0; r < allRows.length; r++) {
      var row = allRows[r];
      var rowId = idColIdx >= 0 ? String(row[idColIdx] || "").trim().toUpperCase().replace(/^'/, "") : "";
      var rowPhone = phoneColIdx >= 0 ? String(row[phoneColIdx] || "").replace(/[^0-9]/g, "").slice(-10) : "";
      var rowName = nameColIdx >= 0 ? String(row[nameColIdx] || "").trim().toLowerCase() : "";

      var isIdMatch = (studentId && rowId && rowId === studentId.toUpperCase());
      var isPhoneMatch = (cleanPhone.length >= 10 && rowPhone.length >= 10 && cleanPhone === rowPhone);
      var isNameMatch = (studentName && rowName && studentName.toLowerCase() === rowName);

      // Strict protection: A different student name or number will NEVER overwrite another student!
      var isExactSameStudent = (isIdMatch && isNameMatch) ||
                               (isPhoneMatch && isNameMatch) ||
                               (isIdMatch && isPhoneMatch);

      if (isExactSameStudent) {
        existingRowIdx = r + 2;
        break;
      }
    }
  }

  var rowData = [];
  var existingVals = existingRowIdx > 0 ? sheet.getRange(existingRowIdx, 1, 1, lastCol).getValues()[0] : null;

  for (var col = 0; col < normalizedHeaders.length; col++) {
    var headerName = normalizedHeaders[col];
    var val = fieldMap[headerName];

    if (val !== undefined && val !== "") {
      rowData.push(val);
    } else {
      var found = false;
      for (var key in fieldMap) {
        if ((headerName.indexOf(key) !== -1 || key.indexOf(headerName) !== -1) && fieldMap[key] !== "") {
          rowData.push(fieldMap[key]);
          found = true;
          break;
        }
      }
      if (!found) {
        rowData.push(existingVals ? existingVals[col] : "");
      }
    }
  }

  if (existingRowIdx > 0) {
    sheet.getRange(existingRowIdx, 1, 1, rowData.length).setValues([rowData]);
  } else {
    sheet.appendRow(rowData);
  }

  return jsonResponse({
    status: "success",
    student_id: studentId,
    student_name: studentName,
    row_action: existingRowIdx > 0 ? "UPDATED_ROW_" + existingRowIdx : "APPENDED_NEW_ROW"
  });
}

function handleGetCoupons(ss) {
  var sheet = ss.getSheetByName("Coupons Creation & Usage") || ss.getSheetByName("Coupons");
  var list = [];
  if (sheet && sheet.getLastRow() > 1) {
    var vals = sheet.getRange(2, 1, sheet.getLastRow() - 1, 7).getValues();
    for (var i = 0; i < vals.length; i++) {
      list.push({
        code: String(vals[i][0]).replace(/^'/, ""),
        discount: vals[i][1],
        max_limit: vals[i][2],
        current_usage: vals[i][3],
        service: vals[i][4],
        active: vals[i][5]
      });
    }
  }
  return jsonResponse({ status: "success", coupons: list });
}

/**
 * Sheet 3: 1-on-1 Counselling Students (DEDUPLICATED UPSERT)
 * A student booking counselling will NEVER create duplicate rows.
 */
function recordInPaidCounsellingSheet(ss, data) {
  try {
    var cSheet = ss.getSheetByName("1-on-1 Counselling Students") || ss.getSheetByName("Paid Counselling Students");
    if (!cSheet) {
      cSheet = ss.insertSheet("1-on-1 Counselling Students");
    }
    if (cSheet.getLastRow() === 0) {
      ensureAllTabsExist(ss);
    }

    var studentId = String(data.ref_id || data.student_id || "").trim().replace(/^'/, "");
    var phone = String(data.phone || data.WhatsApp_Number || "").trim().replace(/^'/, "");
    var cleanPhone = phone.replace(/[^0-9]/g, "");
    if (cleanPhone.length > 10) cleanPhone = cleanPhone.slice(-10);
    var utr = String(data.utr || data.utr_number || "").trim().replace(/^'/, "");

    var timestamp = new Date().toLocaleString("en-IN", { timeZone: "Asia/Kolkata" });
    var studentName = String(data.student_name || data.Name || "Student").trim();
    var parentName = String(data.parent_name || "N/A").trim();
    var email = String(data.email || "N/A").trim();
    var studentClass = String(data.student_class || data.Class || "Class 10").trim();
    var school = String(data.school || "N/A").trim();
    var city = String(data.city || "N/A").trim();
    var rawC = String(data.coupon_code || data.coupon || data["Coupon Code"] || data["Coupon Applied"] || "None").trim();
    var cleanC = (rawC && rawC !== "NONE" && rawC !== "None" && rawC.indexOf("None (Full Fee)") === -1)
      ? rawC.replace(/^COUPON_/, "").split("_")[0].trim().toUpperCase()
      : "";
    if (!cleanC && utr.indexOf("COUPON_") !== -1) {
      cleanC = utr.replace(/^COUPON_/, "").split("_")[0].trim().toUpperCase();
    }
    var couponApplied = cleanC ? cleanC : "None";
    var cleanAmtDigits = String(data.amount || "999").replace(/[^0-9]/g, "");
    var amountPaid = (cleanC.indexOf("100") !== -1 || cleanAmtDigits === "0")
      ? "Paid ₹0 (100% Scholarship: " + couponApplied + ")"
      : "Paid ₹" + (cleanAmtDigits || "999") + (cleanC ? " (" + couponApplied + ")" : "");
    var streamRec = String(data.recommended_stream || "General Guidance").trim();
    var careerRec = String(data.top_careers || "Personalized Consultation").trim();
    var notes = String(data.notes || "1-on-1 Personal Counselling Call").trim();

    // Resolve Cloud Screenshot Proof
    var driveScreenshotUrl = "No Screenshot Attached";
    if (data.image_base64 && String(data.image_base64).trim().length > 100) {
      driveScreenshotUrl = saveScreenshotToDrive(data.image_base64, data.image_mime, "1-on-1 Counselling", studentName, studentId);
    } else if (data.drive_screenshot_url && String(data.drive_screenshot_url).startsWith("http")) {
      driveScreenshotUrl = String(data.drive_screenshot_url);
    } else if (data.screenshot_url && String(data.screenshot_url).startsWith("http")) {
      driveScreenshotUrl = String(data.screenshot_url);
    } else if (data.screenshot_drive_link && String(data.screenshot_drive_link).startsWith("http")) {
      driveScreenshotUrl = String(data.screenshot_drive_link);
    }

    // If still missing, check Sheet 2 for an existing Drive screenshot for this student or UTR
    if (driveScreenshotUrl === "No Screenshot Attached") {
      try {
        var s2 = ss.getSheetByName("Payment Records & Drive Proofs") || ss.getSheetByName("Payment Transactions");
        if (s2 && s2.getLastRow() > 1) {
          var pVals = s2.getRange(2, 1, s2.getLastRow() - 1, Math.min(s2.getLastColumn(), 15)).getValues();
          for (var pi = 0; pi < pVals.length; pi++) {
            var pRow = pVals[pi];
            var pUtr = String(pRow[11] || "").trim().toUpperCase().replace(/^'/, "");
            var pPhone = String(pRow[3] || "").replace(/[^0-9]/g, "").slice(-10);
            var pId = String(pRow[5] || "").trim().toUpperCase().replace(/^'/, "");
            var pUrl = String(pRow[12] || "").trim();
            if (pUrl.startsWith("http")) {
              if ((utr && pUtr && pUtr === utr.toUpperCase()) ||
                  (studentId && pId && pId === studentId.toUpperCase()) ||
                  (cleanPhone.length >= 10 && pPhone.length >= 10 && pPhone === cleanPhone)) {
                driveScreenshotUrl = pUrl;
                break;
              }
            }
          }
        }
      } catch (_) {}
    }

    // CHECK FOR EXISTING ROW IN SHEET 3 TO PREVENT DUPLICATES
    var lastRow = cSheet.getLastRow();
    var existingRowIdx = -1;

    if (lastRow > 1) {
      var cRows = cSheet.getRange(2, 1, lastRow - 1, Math.min(cSheet.getLastColumn(), 17)).getValues();
      for (var c = 0; c < cRows.length; c++) {
        var cr = cRows[c];
        var crPhone = String(cr[2] || "").replace(/[^0-9]/g, "").slice(-10);
        var crUtr = String(cr[9] || "").trim().toUpperCase().replace(/^'/, "");
        var crRefId = String(cr[14] || "").trim().toUpperCase().replace(/^'/, "");

        var isRefMatch = (studentId && crRefId && crRefId === studentId.toUpperCase());
        var isPhoneMatch = (cleanPhone.length >= 10 && crPhone.length >= 10 && crPhone === cleanPhone);
        var isUtrMatch = (utr && utr !== "PENDING_VERIFICATION" && crUtr === utr.toUpperCase());

        var crName = String(cr[0] || "").trim().toLowerCase();
        var isNameMatch = (studentName && crName && studentName.toLowerCase() === crName);
        if (isUtrMatch || (isNameMatch && isPhoneMatch) || (isNameMatch && isRefMatch)) {
          existingRowIdx = c + 2;
          break;
        }
      }
    }

    var rowValues = [
      studentName,
      parentName,
      toPlainTextCell(phone),
      email,
      studentClass,
      school,
      city,
      amountPaid,
      couponApplied,
      toPlainTextCell(utr || "Verified"),
      timestamp,
      "Scheduled",
      streamRec,
      careerRec,
      toPlainTextCell(studentId),
      notes,
      driveScreenshotUrl
    ];

    if (existingRowIdx > 0) {
      var prevProof = String(cRows[existingRowIdx - 2][16] || "").trim();
      if ((!driveScreenshotUrl || !driveScreenshotUrl.startsWith("http")) && prevProof.startsWith("http")) {
        rowValues[16] = prevProof;
      }
      var prevCoupon = String(cRows[existingRowIdx - 2][8] || "").trim();
      if ((couponApplied === "None" || !couponApplied) && prevCoupon && prevCoupon !== "None") {
        rowValues[8] = prevCoupon;
      }
      cSheet.getRange(existingRowIdx, 1, 1, rowValues.length).setValues([rowValues]);
      Logger.log("Deduplicated Counselling booking: updated row " + existingRowIdx);
    } else {
      cSheet.appendRow(rowValues);
      Logger.log("Appended new Counselling booking in Sheet 3");
    }
  } catch (err) {
    Logger.log("Error in recordInPaidCounsellingSheet: " + err);
  }
}

/**
 * 🧹 CLEAN & DEDUPLICATE ALL SHEETS ENGINE
 * Automatically finds duplicate student registrations, duplicate payments,
 * removes duplicate rows, fixes phone formula errors, and recalculates revenue!
 */
function cleanAndDeduplicateAllSheets(ss) {
  var report = {
    fixedPhoneFormulaErrors: 0,
    removedDuplicateStudents: 0,
    removedDuplicatePayments: 0,
    removedDuplicateCounselling: 0
  };

  // Build a student lookup map from Sheet 1 (ID -> Phone, Name -> Phone)
  var studentPhoneMap = {};
  var s1 = ss.getSheetByName("Student Registrations & Profiles") || ss.getSheetByName("Student Dossiers");
  if (s1 && s1.getLastRow() >= 2) {
    var s1Rows = s1.getRange(2, 1, s1.getLastRow() - 1, Math.min(s1.getLastColumn(), 10)).getValues();
    for (var s = 0; s < s1Rows.length; s++) {
      var sRow = s1Rows[s];
      var sid = String(sRow[0] || "").trim().toUpperCase().replace(/^'/, "");
      var sname = String(sRow[2] || "").trim().toLowerCase();
      var sphone = String(sRow[4] || "").replace(/^'/, "").trim();
      if (sphone && sphone.indexOf("#") === -1) {
        if (sid) studentPhoneMap[sid] = sphone;
        if (sname) studentPhoneMap[sname] = sphone;
      }
    }
  }

  // 1. Clean Sheet 2: Duplicate Payments & Revenue Fix
  var s2 = ss.getSheetByName("Payment Records & Drive Proofs") || ss.getSheetByName("Payment Transactions");
  if (s2) {
    try {
      s2.getRange(1, 8).setValue("Net Revenue Collected (Amount Paid)");
      s2.getRange(1, 9).setValue("Catalogue Price");
      s2.getRange(1, 10).setValue("Coupon Discount Waived (Excluded from Revenue)");
      s2.getRange(1, 11).setValue("Coupon Applied");
      s2.getRange("H:J").setNumberFormat('"₹"#,##0');
    } catch (_) {}
  }
  if (s2 && s2.getLastRow() >= 2) {
    // Build screenshot lookup maps from Sheet 2 BEFORE processing
    var paymentScreenshotByUtr = {};
    var paymentScreenshotByPhone = {};
    var paymentScreenshotById = {};
    var pRows = s2.getRange(2, 1, s2.getLastRow() - 1, s2.getLastColumn()).getValues();

    for (var pc = 0; pc < pRows.length; pc++) {
      var pUrl = String(pRows[pc][12] || "").trim();
      if (pUrl.startsWith("http")) {
        var pu = String(pRows[pc][11] || "").trim().toUpperCase().replace(/^'/, "");
        var pp = String(pRows[pc][3] || "").replace(/[^0-9]/g, "").slice(-10);
        var pi = String(pRows[pc][5] || "").trim().toUpperCase().replace(/^'/, "");
        if (pu) paymentScreenshotByUtr[pu] = pUrl;
        if (pp) paymentScreenshotByPhone[pp] = pUrl;
        if (pi) paymentScreenshotById[pi] = pUrl;
      }
    }

    var seenTxns = {};
    var rowsToDelete2 = [];

    for (var i = 0; i < pRows.length; i++) {
      var rowIdx = i + 2;
      var row = pRows[i];
      var sId = String(row[5] || "").trim().toUpperCase().replace(/^'/, "");
      var sName = String(row[1] || "").trim().toLowerCase();

      // Fix phone formula error in cell D
      var phoneRaw = String(row[3] || "");
      if (phoneRaw.indexOf("#") !== -1 || phoneRaw.indexOf("=") !== -1 || !phoneRaw.trim()) {
        var restoredPhone = studentPhoneMap[sId] || studentPhoneMap[sName] || phoneRaw.replace(/[^0-9]/g, "");
        if (restoredPhone) {
          s2.getRange(rowIdx, 4).setValue(toPlainTextCell(restoredPhone));
          report.fixedPhoneFormulaErrors++;
        }
      }

      var utr = String(row[11] || "").trim().toUpperCase().replace(/^'/, "");
      var phoneClean = phoneRaw.replace(/[^0-9]/g, "").slice(-10);
      var svc = String(row[6] || "").trim().toLowerCase();

      // Permanently exclude coupon discounts from Amount Paid in historical rows
      var pCoupon = String(row[10] || "").trim().toUpperCase();
      var origVal = parseFloat(String(row[8] || "0").replace(/[^0-9.]/g, "")) || 0;
      var discVal = parseFloat(String(row[9] || "0").replace(/[^0-9.]/g, "")) || 0;
      var paidVal = parseFloat(String(row[7] || "0").replace(/[^0-9.]/g, "")) || 0;

      if (utr.indexOf("100") !== -1 || pCoupon.indexOf("100") !== -1 || pCoupon === "FREE100" || utr === "COUPON_FREE100") {
        if (origVal <= 0) origVal = 499;
        s2.getRange(rowIdx, 8).setValue(0);
        s2.getRange(rowIdx, 9).setValue(origVal);
        s2.getRange(rowIdx, 10).setValue(origVal);
        if (pCoupon === "-" || pCoupon === "NONE") {
          s2.getRange(rowIdx, 11).setValue(utr.replace("COUPON_", ""));
        }
        report.fixedCouponDiscountWaived = (report.fixedCouponDiscountWaived || 0) + 1;
      } else if (pCoupon && pCoupon !== "-" && pCoupon !== "NONE") {
        if (discVal <= 0 && origVal > 0) {
          if (pCoupon.indexOf("50") !== -1) discVal = Math.round(origVal * 0.5);
        }
        if (discVal > 0 && origVal > 0) {
          var expectedNetCash = Math.max(0, origVal - discVal);
          s2.getRange(rowIdx, 8).setValue(expectedNetCash);
          s2.getRange(rowIdx, 9).setValue(origVal);
          s2.getRange(rowIdx, 10).setValue(discVal);
          report.fixedCouponDiscountWaived = (report.fixedCouponDiscountWaived || 0) + 1;
        }
      }

      // Check and link Drive screenshot if missing
      var currentDriveUrl = String(row[12] || "").trim();
      if (!currentDriveUrl.startsWith("http")) {
        var existingProofUrl = paymentScreenshotByUtr[utr] || paymentScreenshotByPhone[phoneClean] || paymentScreenshotById[sId];
        if (existingProofUrl && existingProofUrl.startsWith("http")) {
          s2.getRange(rowIdx, 13).setValue(existingProofUrl);
          report.linkedPaymentScreenshots = (report.linkedPaymentScreenshots || 0) + 1;
        }
      }

      var key = (utr && utr !== "PENDING_VERIFICATION" && utr !== "MANUAL_VERIFIED") ? utr : ((sId || phoneClean) + "_" + svc);

      if (key && seenTxns[key]) {
        rowsToDelete2.push(rowIdx);
      } else if (key) {
        seenTxns[key] = rowIdx;
      }
    }

    // Delete duplicates from bottom up
    for (var d = rowsToDelete2.length - 1; d >= 0; d--) {
      s2.deleteRow(rowsToDelete2[d]);
      report.removedDuplicatePayments++;
    }
  }

  // 2. Clean Sheet 3: Duplicate Counselling Students & Cloud Screenshot Linking
  var s3 = ss.getSheetByName("1-on-1 Counselling Students") || ss.getSheetByName("Paid Counselling Students");
  if (s3) {
    try {
      s3.getRange(1, 17).setValue("Cloud Screenshot Proof (Google Drive / CDN)");
    } catch (_) {}
  }

  if (s3 && s3.getLastRow() >= 2) {
    var cRows = s3.getRange(2, 1, s3.getLastRow() - 1, Math.min(s3.getLastColumn(), 17)).getValues();
    var seenC = {};
    var rowsToDelete3 = [];

    for (var ci = 0; ci < cRows.length; ci++) {
      var cRowIdx = ci + 2;
      var crow = cRows[ci];
      var cRef = String(crow[14] || "").trim().toUpperCase().replace(/^'/, "");
      var cName = String(crow[0] || "").trim().toLowerCase();
      var cUtr = String(crow[9] || "").trim().toUpperCase().replace(/^'/, "");

      // Fix phone cell
      var cPhoneRaw = String(crow[2] || "");
      if (cPhoneRaw.indexOf("#") !== -1 || cPhoneRaw.indexOf("=") !== -1 || !cPhoneRaw.trim()) {
        var restoredCPhone = studentPhoneMap[cRef] || studentPhoneMap[cName] || cPhoneRaw.replace(/[^0-9]/g, "");
        if (restoredCPhone) {
          s3.getRange(cRowIdx, 3).setValue(toPlainTextCell(restoredCPhone));
          report.fixedPhoneFormulaErrors++;
        }
      }

      // Fix double rupee symbol if present
      var cAmt = String(crow[7] || "");
      if (cAmt.indexOf("₹₹") !== -1) {
        s3.getRange(cRowIdx, 8).setValue(cAmt.replace("₹₹", "₹"));
      }

      var cPhone = cPhoneRaw.replace(/[^0-9]/g, "").slice(-10);
      var cKey = cRef || cPhone;

      // Ensure Cloud Screenshot Proof is populated in Column 17
      var existingProof = String(crow[16] || "").trim();
      if (!existingProof.startsWith("http")) {
        var linkedProof = paymentScreenshotByUtr[cUtr] || paymentScreenshotByPhone[cPhone] || paymentScreenshotById[cRef];
        if (linkedProof && linkedProof.startsWith("http")) {
          s3.getRange(cRowIdx, 17).setValue(linkedProof);
          report.linkedCounsellingScreenshots = (report.linkedCounsellingScreenshots || 0) + 1;
        }
      }

      if (cKey && seenC[cKey]) {
        rowsToDelete3.push(cRowIdx);
      } else if (cKey) {
        seenC[cKey] = cRowIdx;
      }
    }

    for (var cd = rowsToDelete3.length - 1; cd >= 0; cd--) {
      s3.deleteRow(rowsToDelete3[cd]);
      report.removedDuplicateCounselling++;
    }
  }

  // 3. Clean Sheet 1: Duplicate Student Registrations
  if (s1 && s1.getLastRow() >= 2) {
    var sRows = s1.getRange(2, 1, s1.getLastRow() - 1, s1.getLastColumn()).getValues();
    var seenS = {};
    var rowsToDelete1 = [];

    for (var si = 0; si < sRows.length; si++) {
      var sRowIdx = si + 2;
      var srow = sRows[si];

      // Fix phone formula error in cell E
      var sPhoneRaw = String(srow[4] || "");
      if (sPhoneRaw.indexOf("#") !== -1 || sPhoneRaw.indexOf("=") !== -1) {
        s1.getRange(sRowIdx, 5).setValue(toPlainTextCell(sPhoneRaw.replace(/[^0-9]/g, "")));
        report.fixedPhoneFormulaErrors++;
      }

      var sPhone = sPhoneRaw.replace(/[^0-9]/g, "").slice(-10);
      var sRef = String(srow[0] || "").trim().toUpperCase().replace(/^'/, "");
      var sKey = sRef || sPhone;

      if (sKey && seenS[sKey]) {
        rowsToDelete1.push(sRowIdx);
      } else if (sKey) {
        seenS[sKey] = sRowIdx;
      }
    }

    for (var sd = rowsToDelete1.length - 1; sd >= 0; sd--) {
      s1.deleteRow(rowsToDelete1[sd]);
      report.removedDuplicateStudents++;
    }
  }

  ensureAllTabsExist(ss);

  return jsonResponse({
    status: "success",
    message: "Deduplication and sheet cleanup complete.",
    report: report
  });
}

// -------------------------------------------------------------
// ACADEMIC TESTS & STUDENT ACCOUNTS BACKEND (SHEETS 5, 6, 7)
// -------------------------------------------------------------

function handleStudentAccountAuth(ss, data) {
  var s6 = ss.getSheetByName("Student Accounts");
  if (!s6) return jsonResponse({ status: "error", message: "Student Accounts sheet not found" });

  var phone = toPlainTextCell(data.phone || data.phoneNumber || "");
  var email = String(data.email || "").trim();
  var name = String(data.name || "").trim();
  var studentClass = String(data.class || data.studentClass || "10").trim();
  var passwordHash = String(data.passwordHash || "").trim();
  var studentId = String(data.studentId || ("STU_" + phone.replace(/\D/g, "").slice(-10))).trim();

  var lastRow = s6.getLastRow();
  var existingRow = -1;
  if (lastRow > 1) {
    var rows = s6.getRange(2, 1, lastRow - 1, 6).getValues();
    for (var i = 0; i < rows.length; i++) {
      var rPhone = String(rows[i][2]).replace(/^'/, "");
      var rEmail = String(rows[i][3]).trim();
      if ((phone && rPhone === phone.replace(/^'/, "")) || (email && rEmail.toLowerCase() === email.toLowerCase())) {
        existingRow = i + 2;
        break;
      }
    }
  }

  if (existingRow > 0) {
    if (name) s6.getRange(existingRow, 2).setValue(name);
    if (studentClass) s6.getRange(existingRow, 5).setValue(studentClass);
    if (passwordHash) s6.getRange(existingRow, 6).setValue(passwordHash);
    return jsonResponse({
      status: "success",
      message: "Student account verified and updated",
      studentId: String(s6.getRange(existingRow, 1).getValue()).replace(/^'/, ""),
      name: s6.getRange(existingRow, 2).getValue(),
      class: s6.getRange(existingRow, 5).getValue(),
      totalPoints: Number(s6.getRange(existingRow, 7).getValue()) || 0
    });
  } else {
    var nowIso = new Date().toISOString();
    var newRow = [
      toPlainTextCell(studentId),
      name,
      toPlainTextCell(phone),
      email,
      studentClass,
      passwordHash,
      50,
      0,
      0.0,
      nowIso
    ];
    s6.appendRow(newRow);
    return jsonResponse({
      status: "success",
      message: "Student account created successfully with 50 welcome points",
      studentId: studentId,
      name: name,
      class: studentClass,
      totalPoints: 50
    });
  }
}

function handleCreateTest(ss, data) {
  var s5 = ss.getSheetByName("Classwise Tests");
  if (!s5) return jsonResponse({ status: "error", message: "Classwise Tests sheet not found" });

  var targetClass = String(data.targetClass || "10").trim();
  var testId = String(data.testId || ("TEST_C" + targetClass + "_" + Date.now())).trim();
  var subject = String(data.subject || "General").trim();
  var title = String(data.title || "Subject Test").trim();
  var syllabus = String(data.syllabus || "").trim();
  var startTime = String(data.startTime || new Date().toISOString()).trim();
  var endTime = String(data.endTime || new Date(Date.now() + 86400000 * 3).toISOString()).trim();
  var durationMins = Number(data.durationMinutes || 30);
  var totalMarks = Number(data.totalMarks || 10);
  var questionsJson = (typeof data.questions === "string") ? data.questions : JSON.stringify(data.questions || []);
  var status = String(data.status || "ACTIVE").trim();
  var createdAt = new Date().toISOString();

  s5.appendRow([
    toPlainTextCell(testId),
    toPlainTextCell(targetClass),
    subject,
    title,
    syllabus,
    startTime,
    endTime,
    durationMins,
    totalMarks,
    questionsJson,
    status,
    createdAt
  ]);

  return jsonResponse({
    status: "success",
    message: "Test '" + title + "' published successfully for Class " + targetClass,
    testId: testId
  });
}

function handleGetClassTests(ss, data) {
  var s5 = ss.getSheetByName("Classwise Tests");
  if (!s5 || s5.getLastRow() <= 1) {
    return jsonResponse({ status: "success", tests: [] });
  }

  var targetClass = String(data.class || data.targetClass || "10").trim().replace("Class ", "");
  var rows = s5.getRange(2, 1, s5.getLastRow() - 1, 12).getValues();
  var tests = [];
  for (var i = 0; i < rows.length; i++) {
    var r = rows[i];
    var c = String(r[1]).replace(/^'/, "").replace("Class ", "").trim();
    if (c === targetClass || targetClass === "ALL") {
      tests.push({
        id: String(r[0]).replace(/^'/, ""),
        targetClass: c,
        subject: String(r[2]),
        title: String(r[3]),
        syllabus: String(r[4]),
        startTime: String(r[5]),
        endTime: String(r[6]),
        durationMinutes: Number(r[7]) || 30,
        totalMarks: Number(r[8]) || 10,
        questionsJson: String(r[9]),
        status: String(r[10]),
        createdAt: String(r[11])
      });
    }
  }

  return jsonResponse({
    status: "success",
    targetClass: targetClass,
    count: tests.length,
    tests: tests
  });
}

function handleSubmitTest(ss, data) {
  var s7 = ss.getSheetByName("Test Submissions & Results");
  var s6 = ss.getSheetByName("Student Accounts");
  if (!s7 || !s6) return jsonResponse({ status: "error", message: "Required test sheets not found" });

  var submissionId = String(data.submissionId || ("SUB_" + Date.now())).trim();
  var testId = String(data.testId || "").trim();
  var studentId = String(data.studentId || "").trim();
  var studentName = String(data.studentName || "").trim();
  var studentClass = String(data.studentClass || "10").trim();
  var score = Number(data.score || 0);
  var totalMarks = Number(data.totalMarks || 10);
  var accuracyPct = Number(data.accuracyPct || 0);
  var timeTaken = Number(data.timeTakenSeconds || 0);
  var pointsAwarded = Number(data.pointsAwarded || 0);
  var speedBonus = Number(data.speedBonus || 0);
  var growthBonus = Number(data.growthBonus || 0);
  var answersJson = String(data.answersJson || "{}");
  var submittedAt = new Date().toISOString();

  s7.appendRow([
    toPlainTextCell(submissionId),
    toPlainTextCell(testId),
    toPlainTextCell(studentId),
    studentName,
    toPlainTextCell(studentClass),
    score,
    totalMarks,
    accuracyPct,
    timeTaken,
    pointsAwarded,
    speedBonus,
    growthBonus,
    answersJson,
    submittedAt
  ]);

  if (studentId && s6.getLastRow() > 1) {
    var s6Rows = s6.getRange(2, 1, s6.getLastRow() - 1, 10).getValues();
    for (var j = 0; j < s6Rows.length; j++) {
      var rowStuId = String(s6Rows[j][0]).replace(/^'/, "");
      if (rowStuId === studentId) {
        var currPts = Number(s6Rows[j][6]) || 0;
        var currTests = Number(s6Rows[j][7]) || 0;
        s6.getRange(j + 2, 7).setValue(currPts + pointsAwarded);
        s6.getRange(j + 2, 8).setValue(currTests + 1);
        break;
      }
    }
  }

  return jsonResponse({
    status: "success",
    message: "Test submitted! Awarded " + pointsAwarded + " points.",
    submissionId: submissionId,
    pointsAwarded: pointsAwarded
  });
}

function handleGetLeaderboard(ss, data) {
  var s6 = ss.getSheetByName("Student Accounts");
  if (!s6 || s6.getLastRow() <= 1) {
    return jsonResponse({ status: "success", leaderboard: [] });
  }

  var targetClass = String(data.class || data.targetClass || "10").trim().replace("Class ", "");
  var rows = s6.getRange(2, 1, s6.getLastRow() - 1, 9).getValues();
  var leaderboard = [];
  for (var i = 0; i < rows.length; i++) {
    var r = rows[i];
    var c = String(r[4]).replace(/^'/, "").replace("Class ", "").trim();
    if (c === targetClass || targetClass === "ALL") {
      leaderboard.push({
        studentId: String(r[0]).replace(/^'/, ""),
        studentName: String(r[1]),
        studentClass: c,
        totalPoints: Number(r[6]) || 0,
        testsTaken: Number(r[7]) || 0,
        avgAccuracy: Number(r[8]) || 0
      });
    }
  }

  leaderboard.sort(function(a, b) { return b.totalPoints - a.totalPoints; });
  for (var k = 0; k < leaderboard.length; k++) {
    leaderboard[k].rank = k + 1;
  }

  return jsonResponse({
    status: "success",
    targetClass: targetClass,
    count: leaderboard.length,
    leaderboard: leaderboard
  });
}

function handleArchiveTest(ss, data) {
  var s5 = ss.getSheetByName("Classwise Tests");
  if (!s5 || s5.getLastRow() <= 1) return jsonResponse({ status: "error", message: "No tests found" });
  var testId = String(data.testId || "").trim();
  if (!testId) return jsonResponse({ status: "error", message: "Test ID required" });

  var rows = s5.getRange(2, 1, s5.getLastRow() - 1, 11).getValues();
  for (var i = 0; i < rows.length; i++) {
    var id = String(rows[i][0]).replace(/^'/, "");
    if (id === testId) {
      s5.getRange(i + 2, 11).setValue("ARCHIVED");
      return jsonResponse({ status: "success", message: "Test " + testId + " archived successfully." });
    }
  }
  return jsonResponse({ status: "error", message: "Test ID not found" });
}

function handleGetAllTests(ss) {
  var s5 = ss.getSheetByName("Classwise Tests");
  if (!s5 || s5.getLastRow() <= 1) return jsonResponse({ status: "success", count: 0, tests: [] });

  var rows = s5.getRange(2, 1, s5.getLastRow() - 1, 12).getValues();
  var tests = [];
  for (var i = 0; i < rows.length; i++) {
    var r = rows[i];
    tests.push({
      testId: String(r[0]).replace(/^'/, ""),
      targetClass: String(r[1]).replace(/^'/, "").replace("Class ", ""),
      subject: String(r[2]),
      title: String(r[3]),
      syllabus: String(r[4]),
      startTime: String(r[5]),
      endTime: String(r[6]),
      durationMinutes: Number(r[7]) || 30,
      totalMarks: Number(r[8]) || 10,
      questionsCount: (function() {
        try { return JSON.parse(String(r[9])).length; } catch(_) { return 0; }
      })(),
      status: String(r[10]),
      createdAt: String(r[11])
    });
  }
  return jsonResponse({ status: "success", count: tests.length, tests: tests });
}

function handleGetAllSubmissions(ss) {
  var s7 = ss.getSheetByName("Test Submissions & Results");
  if (!s7 || s7.getLastRow() <= 1) return jsonResponse({ status: "success", count: 0, submissions: [] });

  var rows = s7.getRange(2, 1, s7.getLastRow() - 1, 14).getValues();
  var submissions = [];
  for (var i = 0; i < rows.length; i++) {
    var r = rows[i];
    submissions.push({
      submissionId: String(r[0]).replace(/^'/, ""),
      testId: String(r[1]).replace(/^'/, ""),
      studentId: String(r[2]).replace(/^'/, ""),
      studentName: String(r[3]),
      studentClass: String(r[4]).replace(/^'/, ""),
      score: Number(r[5]) || 0,
      totalMarks: Number(r[6]) || 0,
      accuracyPct: Number(r[7]) || 0,
      timeTakenSeconds: Number(r[8]) || 0,
      pointsAwarded: Number(r[9]) || 0,
      speedBonus: Number(r[10]) || 0,
      growthBonus: Number(r[11]) || 0,
      submittedAt: String(r[13])
    });
  }
  submissions.reverse();
  return jsonResponse({ status: "success", count: submissions.length, submissions: submissions });
}


/**
 * Dedicated server-side RPC functions for google.script.run (Teacher Web Portal)
 */
function serverCreateTest(data) {
  var ss = getOrInitSpreadsheet();
  ensureAllTabsExist(ss);
  var s5 = ss.getSheetByName("Classwise Tests");
  if (!s5) return { status: "error", message: "Classwise Tests sheet not found" };

  var targetClass = String(data.targetClass || data.class || "10").trim();
  var subject = String(data.subject || "General").trim();
  var title = String(data.title || "Subject Test").trim();
  var testId = String(data.testId || ("TEST_C" + targetClass + "_" + Date.now())).trim();
  var syllabus = String(data.syllabus || "").trim();
  var startTime = String(data.startTime || new Date().toISOString()).trim();
  var endTime = String(data.endTime || new Date(Date.now() + 7 * 86400000).toISOString()).trim();
  var durationMins = Number(data.durationMinutes || data.durationMins || 25);
  var totalMarks = Number(data.totalMarks || 10);
  var questionsJson = (typeof data.questions === "string") ? data.questions : JSON.stringify(data.questions || []);
  var status = String(data.status || "ACTIVE").trim();
  var createdAt = new Date().toISOString();

  s5.appendRow([
    toPlainTextCell(testId),
    toPlainTextCell(targetClass),
    subject,
    title,
    syllabus,
    startTime,
    endTime,
    durationMins,
    totalMarks,
    questionsJson,
    status,
    createdAt
  ]);

  return {
    status: "success",
    message: "Test \x27" + title + "\x27 published successfully for Class " + targetClass,
    testId: testId
  };
}

function serverGetAllTests() {
  var ss = getOrInitSpreadsheet();
  ensureAllTabsExist(ss);
  var s5 = ss.getSheetByName("Classwise Tests");
  if (!s5 || s5.getLastRow() <= 1) return { status: "success", count: 0, tests: [] };

  var rows = s5.getRange(2, 1, s5.getLastRow() - 1, 12).getValues();
  var tests = [];
  for (var i = 0; i < rows.length; i++) {
    var r = rows[i];
    var qCount = 0;
    try { qCount = JSON.parse(String(r[9])).length; } catch (_) {}
    tests.push({
      testId: String(r[0]).replace(/^'/, ""),
      targetClass: String(r[1]).replace(/^'/, ""),
      subject: String(r[2]),
      title: String(r[3]),
      syllabus: String(r[4]),
      startTime: String(r[5]),
      endTime: String(r[6]),
      durationMinutes: Number(r[7]) || 25,
      totalMarks: Number(r[8]) || 0,
      questionsCount: qCount,
      status: String(r[10]),
      createdAt: String(r[11])
    });
  }
  tests.reverse();
  return { status: "success", count: tests.length, tests: tests };
}

function serverArchiveTest(id) {
  var ss = getOrInitSpreadsheet();
  ensureAllTabsExist(ss);
  var s5 = ss.getSheetByName("Classwise Tests");
  if (!s5 || s5.getLastRow() <= 1) return { status: "error", message: "No tests found" };

  var rows = s5.getRange(2, 1, s5.getLastRow() - 1, 1).getValues();
  for (var i = 0; i < rows.length; i++) {
    var testId = String(rows[i][0]).replace(/^'/, "").trim();
    if (testId === String(id).trim()) {
      s5.getRange(i + 2, 11).setValue("ARCHIVED");
      return { status: "success", message: "Test " + id + " has been archived." };
    }
  }
  return { status: "error", message: "Test ID not found" };
}

function serverGetAllSubmissions() {
  var ss = getOrInitSpreadsheet();
  ensureAllTabsExist(ss);
  var s7 = ss.getSheetByName("Test Submissions & Results");
  if (!s7 || s7.getLastRow() <= 1) return { status: "success", count: 0, submissions: [] };

  var rows = s7.getRange(2, 1, s7.getLastRow() - 1, 14).getValues();
  var submissions = [];
  for (var i = 0; i < rows.length; i++) {
    var r = rows[i];
    submissions.push({
      submissionId: String(r[0]).replace(/^'/, ""),
      testId: String(r[1]).replace(/^'/, ""),
      studentId: String(r[2]).replace(/^'/, ""),
      studentName: String(r[3]),
      studentClass: String(r[4]).replace(/^'/, ""),
      score: Number(r[5]) || 0,
      totalMarks: Number(r[6]) || 0,
      accuracyPct: Number(r[7]) || 0,
      timeTakenSeconds: Number(r[8]) || 0,
      pointsAwarded: Number(r[9]) || 0,
      speedBonus: Number(r[10]) || 0,
      growthBonus: Number(r[11]) || 0,
      submittedAt: String(r[13])
    });
  }
  submissions.reverse();
  return { status: "success", count: submissions.length, submissions: submissions };
}

function renderTeacherWebPortal(ss) {
  var sheetLink = ss.getUrl();
  var s5 = ss.getSheetByName("Classwise Tests");
  var existingTestsCount = s5 ? Math.max(0, s5.getLastRow() - 1) : 0;

  var html = '<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">';
  html += '<title>MargDarsh Teacher Test Creator Portal</title>';
  html += '<style>';
  html += 'body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #0B1528; color: #E2E8F0; margin: 0; padding: 20px; line-height: 1.5; }';
  html += '.container { max-width: 920px; margin: 0 auto; }';
  html += '.header { background: #152238; border: 1px solid #1E3A8A; border-radius: 16px; padding: 24px; margin-bottom: 20px; box-shadow: 0 8px 20px rgba(0,0,0,0.4); }';
  html += '.badge { display: inline-block; background: #064E3B; color: #34D399; font-weight: bold; padding: 4px 12px; border-radius: 999px; font-size: 12px; margin-bottom: 10px; }';
  html += 'h1 { color: #FFFFFF; font-size: 24px; margin: 0 0 8px 0; }';
  html += 'p { color: #94A3B8; font-size: 14px; margin: 0 0 16px 0; }';
  html += '.nav-tabs { display: flex; gap: 8px; margin-bottom: 20px; border-bottom: 1px solid #1E293B; padding-bottom: 10px; }';
  html += '.nav-btn { background: #152238; border: 1px solid #334155; color: #94A3B8; padding: 10px 18px; border-radius: 8px; cursor: pointer; font-weight: 600; font-size: 14px; }';
  html += '.nav-btn.active { background: #2563EB; color: #FFFFFF; border-color: #2563EB; }';
  html += '.card { background: #111E36; border: 1px solid #1E293B; border-radius: 12px; padding: 20px; margin-bottom: 20px; }';
  html += '.form-group { margin-bottom: 16px; }';
  html += 'label { display: block; font-size: 13px; font-weight: 600; color: #CBD5E1; margin-bottom: 6px; }';
  html += 'input, select, textarea { width: 100%; box-sizing: border-box; background: #0B1528; border: 1px solid #334155; border-radius: 8px; color: #FFFFFF; padding: 10px 12px; font-size: 14px; }';
  html += 'input:focus, select:focus, textarea:focus { border-color: #38BDF8; outline: none; }';
  html += '.row { display: flex; gap: 16px; flex-wrap: wrap; }';
  html += '.col { flex: 1; min-width: 220px; }';
  html += '.q-box { background: #0E1A2E; border: 1px solid #1E3A8A; border-radius: 10px; padding: 16px; margin-bottom: 16px; position: relative; }';
  html += '.q-box-title { font-weight: bold; color: #38BDF8; font-size: 14px; margin-bottom: 12px; }';
  html += '.opt-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 12px; }';
  html += '.btn { display: inline-block; background: #2563EB; color: #FFFFFF; border: none; font-weight: 600; padding: 12px 24px; border-radius: 8px; cursor: pointer; font-size: 14px; transition: background 0.2s; }';
  html += '.btn:hover { background: #1D4ED8; }';
  html += '.btn-secondary { background: #334155; margin-right: 8px; }';
  html += '.btn-secondary:hover { background: #475569; }';
  html += '.btn-delete { background: #EF4444; color: #fff; padding: 4px 10px; font-size: 12px; border-radius: 6px; float: right; cursor: pointer; }';
  html += '.status-box { padding: 12px; border-radius: 8px; margin-top: 16px; display: none; }';
  html += 'table { width: 100%; border-collapse: collapse; margin-top: 12px; }';
  html += 'th, td { padding: 10px 12px; text-align: left; border-bottom: 1px solid #1E293B; font-size: 13px; }';
  html += 'th { background: #0E1A2E; color: #94A3B8; font-weight: 600; }';
  html += 'tr:hover { background: #152238; }';
  html += '</style></head><body>';
  html += '<div class="container">';
  html += '<div class="header">';
  html += '<div class="badge">● TEACHER ADMIN CONSOLE</div>';
  html += '<h1>MargDarsh Academic MCQ Test Portal</h1>';
  html += '<p>Create, schedule, and analyze class-wise tests for high school students (Classes 8–12). Published tests appear instantly in the student app!</p>';
  html += '<a class="btn btn-secondary" style="text-decoration:none;" href="' + sheetLink + '" target="_blank">📊 View Google Sheet (' + existingTestsCount + ' Tests)</a>';
  html += '<a class="btn btn-secondary" style="text-decoration:none;" href="?page=admin">⚙️ Main Admin Dashboard</a>';
  html += '</div>';

  html += '<div class="nav-tabs">';
  html += '<button type="button" class="nav-btn active" onclick="switchTab(\'create\')">📝 Create & Publish Test</button>';
  html += '<button type="button" class="nav-btn" onclick="switchTab(\'tests\')">📋 Published Tests (' + existingTestsCount + ')</button>';
  html += '<button type="button" class="nav-btn" onclick="switchTab(\'submissions\')">🏆 Student Submissions & Results</button>';
  html += '</div>';

  // TAB 1: CREATE TEST
  html += '<div id="tabCreate">';
  html += '<form id="testForm" onsubmit="submitNewTest(event)">';
  html += '<div class="card">';
  html += '<h3 style="color:#38BDF8;margin-top:0;">1. Basic Test Details</h3>';
  html += '<div class="row">';
  html += '<div class="col"><div class="form-group"><label>Target Class *</label><select id="targetClass" required><option value="8">Class 8</option><option value="9">Class 9</option><option value="10" selected>Class 10</option><option value="11">Class 11</option><option value="12">Class 12</option></select></div></div>';
  html += '<div class="col"><div class="form-group"><label>Subject *</label><input type="text" id="subject" placeholder="e.g. Mathematics, Science, English" required></div></div>';
  html += '<div class="col"><div class="form-group"><label>Duration (Minutes) *</label><input type="number" id="durationMinutes" value="25" min="5" max="180" required></div></div>';
  html += '</div>';
  html += '<div class="form-group"><label>Test Title *</label><input type="text" id="title" placeholder="e.g. Quadratic Equations & Arithmetic Progressions" required></div>';
  html += '<div class="form-group"><label>Syllabus & Topics *</label><textarea id="syllabus" rows="3" placeholder="List the chapters, theorems, or formula topics covered in this test..." required></textarea></div>';
  html += '<div class="row">';
  html += '<div class="col"><div class="form-group"><label>Start Date & Time (IST) *</label><input type="datetime-local" id="startTime" required></div></div>';
  html += '<div class="col"><div class="form-group"><label>End Date & Time (IST) *</label><input type="datetime-local" id="endTime" required></div></div>';
  html += '</div>';
  html += '</div>';

  html += '<div class="card">';
  html += '<div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">';
  html += '<h3 style="color:#38BDF8;margin:0;">2. MCQ Questions & Explanations</h3>';
  html += '<button type="button" class="btn btn-secondary" onclick="addQuestionBox()">+ Add Question</button>';
  html += '</div>';
  html += '<div id="questionsContainer"></div>';
  html += '</div>';

  html += '<div style="margin-bottom:40px;">';
  html += '<button type="submit" id="submitBtn" class="btn" style="background:#059669;font-size:16px;padding:14px 28px;">🚀 Publish Test to MargDarsh App</button>';
  html += '<div id="statusBox" class="status-box"></div>';
  html += '</div>';
  html += '</form>';
  html += '</div>';

  // TAB 2: PUBLISHED TESTS
  html += '<div id="tabTests" style="display:none;">';
  html += '<div class="card">';
  html += '<h3 style="color:#38BDF8;margin-top:0;">Published Tests in Sheet 5</h3>';
  html += '<div id="testsTableContainer">Loading published tests...</div>';
  html += '</div>';
  html += '</div>';

  // TAB 3: SUBMISSIONS
  html += '<div id="tabSubmissions" style="display:none;">';
  html += '<div class="card">';
  html += '<h3 style="color:#38BDF8;margin-top:0;">Student Exam Submissions & Accuracy</h3>';
  html += '<div id="submissionsTableContainer">Loading submissions...</div>';
  html += '</div>';
  html += '</div>';

  html += '<script>';
  html += 'var questionCount = 0;';
  html += 'function switchTab(t) {';
  html += '  document.getElementById("tabCreate").style.display = (t === "create") ? "block" : "none";';
  html += '  document.getElementById("tabTests").style.display = (t === "tests") ? "block" : "none";';
  html += '  document.getElementById("tabSubmissions").style.display = (t === "submissions") ? "block" : "none";';
  html += '  var btns = document.querySelectorAll(".nav-btn");';
  html += '  btns[0].className = "nav-btn" + (t === "create" ? " active" : "");';
  html += '  btns[1].className = "nav-btn" + (t === "tests" ? " active" : "");';
  html += '  btns[2].className = "nav-btn" + (t === "submissions" ? " active" : "");';
  html += '  if (t === "tests") loadPublishedTests();';
  html += '  if (t === "submissions") loadSubmissions();';
  html += '}';
  html += 'function addQuestionBox() {';
  html += '  questionCount++;';
  html += '  var qId = questionCount;';
  html += '  var div = document.createElement("div");';
  html += '  div.className = "q-box";';
  html += '  div.id = "qBox_" + qId;';
  html += '  var h = "<div class=\\"q-box-title\\">Question " + qId + " <button type=\\"button\\" class=\\"btn-delete\\" onclick=\\"document.getElementById(\'qBox_\' + " + qId + ").remove()\\">Delete</button></div>";';
  html += '  h += "<div class=\\"form-group\\"><label>Question Text *</label><textarea class=\\"q-text\\" rows=\\"2\\" placeholder=\\"Enter question statement here...\\" required></textarea></div>";';
  html += '  h += "<div class=\\"opt-grid\\">";';
  html += '  h += "<div><label>Option A *</label><input type=\\"text\\" class=\\"opt-a\\" placeholder=\\"Option A\\" required></div>";';
  html += '  h += "<div><label>Option B *</label><input type=\\"text\\" class=\\"opt-b\\" placeholder=\\"Option B\\" required></div>";';
  html += '  h += "<div><label>Option C *</label><input type=\\"text\\" class=\\"opt-c\\" placeholder=\\"Option C\\" required></div>";';
  html += '  h += "<div><label>Option D *</label><input type=\\"text\\" class=\\"opt-d\\" placeholder=\\"Option D\\" required></div>";';
  html += '  h += "</div>";';
  html += '  h += "<div class=\\"row\\"><div class=\\"col\\"><div class=\\"form-group\\"><label>Correct Answer *</label><select class=\\"correct-opt\\"><option value=\\"0\\">Option A</option><option value=\\"1\\">Option B</option><option value=\\"2\\">Option C</option><option value=\\"3\\">Option D</option></select></div></div>";';
  html += '  h += "<div class=\\"col\\"><div class=\\"form-group\\"><label>Marks</label><input type=\\"number\\" class=\\"q-marks\\" value=\\"1\\" min=\\"1\\"></div></div></div>";';
  html += '  h += "<div class=\\"form-group\\"><label>Step-by-Step Explanation *</label><textarea class=\\"q-exp\\" rows=\\"2\\" placeholder=\\"Explain why this is correct so students learn from their mistakes...\\" required></textarea></div>";';
  html += '  div.innerHTML = h;';
  html += '  document.getElementById("questionsContainer").appendChild(div);';
  html += '}';
  html += 'window.onload = function() {';
  html += '  var now = new Date();';
  html += '  var end = new Date(now.getTime() + 48*3600*1000);';
  html += '  document.getElementById("startTime").value = now.toISOString().slice(0, 16);';
  html += '  document.getElementById("endTime").value = end.toISOString().slice(0, 16);';
  html += '  addQuestionBox();';
  html += '  addQuestionBox();';
  html += '};';
  html += 'function submitNewTest(e) {';
  html += '  e.preventDefault();';
  html += '  var btn = document.getElementById("submitBtn");';
  html += '  var status = document.getElementById("statusBox");';
  html += '  var qBoxes = document.querySelectorAll(".q-box");';
  html += '  if (qBoxes.length === 0) { alert("Please add at least one question!"); return; }';
  html += '  var questions = [];';
  html += '  for (var i = 0; i < qBoxes.length; i++) {';
  html += '    var b = qBoxes[i];';
  html += '    questions.push({';
  html += '      id: "Q" + (i + 1),';
  html += '      question: b.querySelector(".q-text").value.trim(),';
  html += '      options: [b.querySelector(".opt-a").value.trim(), b.querySelector(".opt-b").value.trim(), b.querySelector(".opt-c").value.trim(), b.querySelector(".opt-d").value.trim()],';
  html += '      correctIndex: parseInt(b.querySelector(".correct-opt").value),';
  html += '      marks: parseInt(b.querySelector(".q-marks").value) || 1,';
  html += '      explanation: b.querySelector(".q-exp").value.trim()';
  html += '    });';
  html += '  }';
  html += '  var payload = {';
  html += '    action: "CREATE_TEST",';
  html += '    targetClass: document.getElementById("targetClass").value,';
  html += '    subject: document.getElementById("subject").value.trim(),';
  html += '    durationMinutes: parseInt(document.getElementById("durationMinutes").value) || 25,';
  html += '    title: document.getElementById("title").value.trim(),';
  html += '    syllabus: document.getElementById("syllabus").value.trim(),';
  html += '    startTime: new Date(document.getElementById("startTime").value).toISOString(),';
  html += '    endTime: new Date(document.getElementById("endTime").value).toISOString(),';
  html += '    totalMarks: questions.reduce(function(acc, cur) { return acc + cur.marks; }, 0),';
  html += '    questions: JSON.stringify(questions)';
  html += '  };';
  html += '  btn.disabled = true; btn.innerText = "Publishing to MargDarsh App...";';
  html += '  if (typeof google !== "undefined" && google.script && google.script.run) {';
  html += '    google.script.run';
  html += '      .withSuccessHandler(function(data) {';
  html += '        btn.disabled = false; btn.innerText = "🚀 Publish Test to MargDarsh App";';
  html += '        if (data.status === "success") {';
  html += '          status.style.background = "#064E3B"; status.style.color = "#34D399";';
  html += '          status.innerHTML = "✓ Test successfully published to Sheet 5! Students in Class " + payload.targetClass + " will see it immediately in their MargDarsh App.";';
  html += '          document.getElementById("title").value = "";';
  html += '          document.getElementById("syllabus").value = "";';
  html += '        } else {';
  html += '          status.style.background = "#7F1D1D"; status.style.color = "#FCA5A5";';
  html += '          status.innerText = "Error: " + (data.message || "Failed to publish test");';
  html += '        }';
  html += '      })';
  html += '      .withFailureHandler(function(err) {';
  html += '        btn.disabled = false; btn.innerText = "🚀 Publish Test to MargDarsh App";';
  html += '        status.style.background = "#7F1D1D"; status.style.color = "#FCA5A5";';
  html += '        status.innerText = "Error: " + err.toString();';
  html += '      })';
  html += '      .serverCreateTest(payload);';
  html += '  }';
  html += '}';
  html += 'function loadPublishedTests() {';
  html += '  var c = document.getElementById("testsTableContainer");';
  html += '  c.innerHTML = "Fetching tests from Sheet 5...";';
  html += '  if (typeof google !== "undefined" && google.script && google.script.run) {';
  html += '    google.script.run';
  html += '      .withSuccessHandler(function(data) {';
  html += '        if (!data.tests || data.tests.length === 0) { c.innerHTML = "No tests published yet."; return; }';
  html += '        var t = "<table><thead><tr><th>Class</th><th>Subject</th><th>Title</th><th>Duration</th><th>Questions</th><th>Status</th><th>Action</th></tr></thead><tbody>";';
  html += '        data.tests.forEach(function(x) {';
  html += '          t += "<tr><td>Class " + x.targetClass + "</td><td>" + x.subject + "</td><td><strong>" + x.title + "</strong></td><td>" + x.durationMinutes + "m</td><td>" + x.questionsCount + " Qs</td><td>" + x.status + "</td><td><button class=\'btn-delete\' onclick=\'archiveTest(\\\"" + x.testId + "\\\")\'>Archive</button></td></tr>";';
  html += '        });';
  html += '        t += "</tbody></table>";';
  html += '        c.innerHTML = t;';
  html += '      })';
  html += '      .withFailureHandler(function(err) { c.innerHTML = "Error loading tests: " + err.toString(); })';
  html += '      .serverGetAllTests();';
  html += '  }';
  html += '}';
  html += 'function archiveTest(id) {';
  html += '  if (!confirm("Are you sure you want to archive test " + id + "?")) return;';
  html += '  if (typeof google !== "undefined" && google.script && google.script.run) {';
  html += '    google.script.run';
  html += '      .withSuccessHandler(function(data) { alert(data.message || "Archived"); loadPublishedTests(); })';
  html += '      .withFailureHandler(function(err) { alert("Error: " + err.toString()); })';
  html += '      .serverArchiveTest(id);';
  html += '  }';
  html += '}';
  html += 'function loadSubmissions() {';
  html += '  var c = document.getElementById("submissionsTableContainer");';
  html += '  c.innerHTML = "Fetching submissions from Sheet 7...";';
  html += '  if (typeof google !== "undefined" && google.script && google.script.run) {';
  html += '    google.script.run';
  html += '      .withSuccessHandler(function(data) {';
  html += '        if (!data.submissions || data.submissions.length === 0) { c.innerHTML = "No test submissions logged yet."; return; }';
  html += '        var t = "<table><thead><tr><th>Student</th><th>Class</th><th>Score</th><th>Accuracy</th><th>Time</th><th>Points</th><th>Bonuses</th></tr></thead><tbody>";';
  html += '        data.submissions.forEach(function(s) {';
  html += '          var bonuses = (s.speedBonus > 0 ? "⚡ Speed (+" + s.speedBonus + ") " : "") + (s.growthBonus > 0 ? "📈 Growth (+" + s.growthBonus + ")" : "");';
  html += '          t += "<tr><td><strong>" + s.studentName + "</strong></td><td>Class " + s.studentClass + "</td><td>" + s.score + "/" + s.totalMarks + "</td><td>" + s.accuracyPct.toFixed(1) + "%</td><td>" + Math.floor(s.timeTakenSeconds/60) + "m " + (s.timeTakenSeconds%60) + "s</td><td>+" + s.pointsAwarded + " pts</td><td>" + (bonuses || "-") + "</td></tr>";';
  html += '        });';
  html += '        t += "</tbody></table>";';
  html += '        c.innerHTML = t;';
  html += '      })';
  html += '      .withFailureHandler(function(err) { c.innerHTML = "Error loading submissions: " + err.toString(); })';
  html += '      .serverGetAllSubmissions();';
  html += '  }';
  html += '}';
  html += '</script></div></body></html>';

  return HtmlService.createHtmlOutput(html).setXFrameOptionsMode(HtmlService.XFrameOptionsMode.ALLOWALL);
}

/**
 * Beautiful HTML Status Dashboard (7 Sheets)
 */
function renderWebDashboard(ss) {
  var folder = getOrCreateDriveFolder();
  var driveLink = folder ? folder.getUrl() : "#";
  var sheetLink = ss.getUrl();
  var s1Count = Math.max(0, (ss.getSheetByName("Student Registrations & Profiles") ? ss.getSheetByName("Student Registrations & Profiles").getLastRow() - 1 : 0));
  var s2Count = Math.max(0, (ss.getSheetByName("Payment Records & Drive Proofs") ? ss.getSheetByName("Payment Records & Drive Proofs").getLastRow() - 1 : 0));
  var s3Count = Math.max(0, (ss.getSheetByName("1-on-1 Counselling Students") ? ss.getSheetByName("1-on-1 Counselling Students").getLastRow() - 1 : 0));
  var s4Count = Math.max(0, (ss.getSheetByName("Coupons Creation & Usage") ? ss.getSheetByName("Coupons Creation & Usage").getLastRow() - 1 : 0));
  var s5Count = Math.max(0, (ss.getSheetByName("Classwise Tests") ? ss.getSheetByName("Classwise Tests").getLastRow() - 1 : 0));
  var s6Count = Math.max(0, (ss.getSheetByName("Student Accounts") ? ss.getSheetByName("Student Accounts").getLastRow() - 1 : 0));
  var s7Count = Math.max(0, (ss.getSheetByName("Test Submissions & Results") ? ss.getSheetByName("Test Submissions & Results").getLastRow() - 1 : 0));

  var html = '<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">';
  html += '<title>MargDarsh Enterprise 7-Sheet Server</title>';
  html += '<style>';
  html += 'body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #0B1528; color: #E2E8F0; margin: 0; padding: 24px; }';
  html += '.card { background: #152238; border: 1px solid #1E3A8A; border-radius: 16px; padding: 24px; max-width: 820px; margin: 0 auto; box-shadow: 0 10px 25px rgba(0,0,0,0.5); }';
  html += '.badge { display: inline-block; background: #064E3B; color: #34D399; font-weight: bold; padding: 6px 14px; border-radius: 999px; font-size: 13px; margin-bottom: 12px; }';
  html += 'h1 { color: #FFFFFF; font-size: 24px; margin: 0 0 8px 0; }';
  html += 'p { color: #94A3B8; font-size: 14px; line-height: 1.6; margin: 0 0 20px 0; }';
  html += '.grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: 12px; margin-bottom: 24px; }';
  html += '.tile { background: #0E1A2E; border: 1px solid #1E293B; border-radius: 12px; padding: 14px; text-align: center; }';
  html += '.tile-title { font-size: 11px; text-transform: uppercase; color: #64748B; font-weight: bold; margin-bottom: 4px; }';
  html += '.tile-val { font-size: 22px; font-weight: bold; color: #38BDF8; }';
  html += '.btn { display: inline-block; background: #2563EB; color: #FFFFFF; text-decoration: none; font-weight: 600; padding: 12px 20px; border-radius: 10px; margin-right: 10px; margin-bottom: 10px; transition: background 0.2s; }';
  html += '.btn:hover { background: #1D4ED8; }';
  html += '.btn-teacher { background: #D97706; }';
  html += '.btn-teacher:hover { background: #B45309; }';
  html += '.btn-drive { background: #059669; }';
  html += '.btn-drive:hover { background: #047857; }';
  html += '.btn-clean { background: #7C3AED; }';
  html += '.btn-clean:hover { background: #6D28D9; }';
  html += '.btn-outline { background: transparent; border: 1px solid #475569; color: #CBD5E1; }';
  html += '.btn-outline:hover { background: #1E293B; }';
  html += '.footer { font-size: 12px; color: #64748B; margin-top: 24px; border-top: 1px solid #1E293B; padding-top: 16px; }';
  html += '</style></head><body>';
  html += '<div class="card">';
  html += '<div class="badge">● SERVER ACTIVE & DEDUPLICATED (7 SHEETS)</div>';
  html += '<h1>MargDarsh Enterprise 7-Sheet Server</h1>';
  html += '<p>Google Apps Script Webhook is active with dedicated tables for tests, student credentials, deduplicated payments, and psychometric audits.</p>';
  html += '<div class="grid">';
  html += '<div class="tile"><div class="tile-title">Sheet 1: Profiles</div><div class="tile-val">' + s1Count + '</div></div>';
  html += '<div class="tile"><div class="tile-title">Sheet 2: Payments</div><div class="tile-val">' + s2Count + '</div></div>';
  html += '<div class="tile"><div class="tile-title">Sheet 3: Counsel</div><div class="tile-val">' + s3Count + '</div></div>';
  html += '<div class="tile"><div class="tile-title">Sheet 4: Coupons</div><div class="tile-val">' + s4Count + '</div></div>';
  html += '<div class="tile"><div class="tile-title">Sheet 5: Tests</div><div class="tile-val">' + s5Count + '</div></div>';
  html += '<div class="tile"><div class="tile-title">Sheet 6: Accounts</div><div class="tile-val">' + s6Count + '</div></div>';
  html += '<div class="tile"><div class="tile-title">Sheet 7: Submissions</div><div class="tile-val">' + s7Count + '</div></div>';
  html += '</div>';
  html += '<div style="margin-bottom: 16px;">';
  html += '<a class="btn btn-teacher" href="?page=teacher" target="_blank">📝 Open Teacher Test Portal</a>';
  html += '<a class="btn" href="' + sheetLink + '" target="_blank">📊 Open Google Sheet</a>';
  html += '<a class="btn btn-drive" href="' + driveLink + '" target="_blank">📂 Open Drive Screenshots Folder</a>';
  html += '<a class="btn btn-clean" href="?action=CLEAN_DUPLICATES" target="_blank">🧹 Clean All Duplicates Now</a>';
  html += '<a class="btn btn-outline" href="?action=GET_ADMIN_DATA" target="_blank">⚡ Test Live JSON API</a>';
  html += '</div>';
  html += '<div class="footer">';
  html += '<strong>Spreadsheet:</strong> ' + ss.getName() + '<br>';
  html += '<strong>Target Folder:</strong> ' + DRIVE_FOLDER_NAME + '<br>';
  html += '<strong>Server Time:</strong> ' + new Date().toLocaleString("en-IN", { timeZone: "Asia/Kolkata" }) + ' (IST)';
  html += '</div></div></body></html>';

  return HtmlService.createHtmlOutput(html).setXFrameOptionsMode(HtmlService.XFrameOptionsMode.ALLOWALL);
}

function jsonResponse(obj) {
  return ContentService.createTextOutput(JSON.stringify(obj))
    .setMimeType(ContentService.MimeType.JSON);
}
""".trimIndent()
}
