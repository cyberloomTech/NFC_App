# NFC App

A simple NFC reader/writer Android app built with **Kotlin** and **Jetpack Compose**, optimized for NTAG216 tags.


## ✨ Features
- Read and write NDEF text records to NFC tags
- Optional tag locking (make tag read-only)
- Haptic feedback (vibration) on successful read/write
- Clipboard integration (copy & clear)
- Jetpack Compose UI with smooth navigation


## 🛠️ Requirements
- Android 5.0 (API 21) or higher
- A physical device with NFC support (emulator **cannot** emulate NFC)


## 🚀 Building the App
1. Clone or extract the project.
2. Open the folder in **NFC_App**.
3. Connect an NFC-enabled Android device.
4. Enable **NFC** on the phone.
5. Press **Run ▶** to install and launch the app.


## 📲 How to Use
1. Open the app — two screens appear: **READER** and **WRITER**.
2. To read: hold an NFC tag near your phone’s NFC sensor.
3. To write:
   - Enter text and tap **WRITE NFC TAG**.
   - Hold a tag to the phone.
   - Wait for vibration feedback.
4. To lock the tag, check “WRITE-PROTECT NFC TAG”


## 🔔 Notes
- NFC simulation does not work on Android Emulator.
- All feedback is via vibration — **no sound is used**.
- For NTAG216, capacity ≈ 888 bytes of text data.


## 🧾 License

MIT License  
Copyright (c) 2025

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.