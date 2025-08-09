* **Overview**
* **Hardware recommendations**
* **Software requirements**
* **Database structure**
* **Enrollment & verification workflows**
* **Budget-friendly implementation approach**

---

````markdown
# Biometric Attendance System Plan (v1)

## 1. Overview
This document outlines the plan to integrate both **fingerprint recognition** and **facial recognition** into our JavaFX + MySQL payroll system.  
The goal is to improve accuracy in time-in/out logs, prevent buddy punching, and store biometric data securely in our existing database.

---

## 2. Hardware Requirements

| Purpose            | Recommended Device | Approx. Cost (PHP) | Notes |
|--------------------|--------------------|--------------------|-------|
| Fingerprint Scanner | Digital Persona U.are.U 4500 | ₱2,500 – ₱3,000 | Popular, reliable, Java SDK available |
| Camera              | Logitech C920 or generic HD USB webcam | ₱700 – ₱1,200 | Works with OpenCV for Java face recognition |
| Computer            | Existing PC or low-power mini PC | — | Runs JavaFX app & matching software |

---

## 3. Software Requirements

- **JavaFX** (existing UI framework)
- **MySQL** (existing DB)
- **Fingerprint SDK** from device vendor (Java API/JNI support)
- **OpenCV** for Java (face detection & recognition)
- **Optional**: Local Python microservice using `face_recognition` library for higher accuracy face matching

---

## 4. Database Changes

### New Tables:

CREATE TABLE attendance (
    id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    fingerprint_match BOOLEAN,
    face_match BOOLEAN,
    photo_path VARCHAR(255),
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);
````

---

## 5. Enrollment Workflow

1. **Capture Fingerprint**

   * Use fingerprint scanner SDK to capture fingerprint.
   * Convert to **template** (SDK-provided format).
   * Store encrypted template in `biometric_data.fingerprint_template`.

2. **Capture Face**

   * Use webcam + OpenCV to detect face.
   * Generate **face encoding** (128-d vector).
   * Store encoding in `biometric_data.face_encoding`.

3. **Store Record**

   * Save both templates in MySQL under employee’s ID.

---

## 6. Verification Workflow

**When an employee clocks in/out:**

1. **Fingerprint Scan**

   * Capture live scan, match with stored template.
   * If matched, proceed; else reject.

2. **Face Verification**

   * Capture live webcam photo.
   * Compare live face encoding to stored encoding.
   * If matched, proceed; else reject.

3. **Log Attendance**

   * Save timestamp, fingerprint\_match, face\_match, and captured photo path in `attendance`.

---

## 7. Budget-Friendly Implementation Strategy

* **Phase 1:** Implement fingerprint recognition first (easiest, direct SDK integration).
* **Phase 2:** Add facial recognition using OpenCV.
* **Phase 3:** Optimize recognition accuracy and add anti-spoofing measures.

---

## 8. Estimated Cost

| Item                         | Quantity | Unit Price (PHP) | Total (PHP) |
| ---------------------------- | -------- | ---------------- | ----------- |
| Digital Persona U.are.U 4500 | 1        | 2,800            | 2,800       |
| HD USB Webcam                | 1        | 1,000            | 1,000       |
| **Total**                    | —        | —                | **3,800**   |

---

## 9. Future Improvements

* Use **liveness detection** to prevent spoofing via photos/videos.
* Switch to **all-in-one biometric terminal** if budget increases.
* Encrypt biometric data using AES before storing in DB.

---

*Document version:* **v1**
*Author:* Jomari Abejo
*Date:* `2025-11-09`
