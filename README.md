# Credit Simulator

Aplikasi console untuk menghitung cicilan bulanan kredit kendaraan (Mobil dan Motor).

## Menjalankan

Butuh JDK 21 dan Maven.

```text
mvn clean package
```

JAR ada di `target/credit-simulator.jar`.

### Linux / macOS

```text
chmod +x credit_simulator
./credit_simulator
./credit_simulator file_inputs.txt
```

Jalur `bin/` juga bisa:

```text
bin/credit_simulator
bin/credit_simulator file_inputs.txt
```

### Windows

```text
credit_simulator.bat
credit_simulator.bat file_inputs.txt
```

Kalau JAR belum ada, skrip akan menjalankan `mvn clean package -DskipTests` dulu.

## Mode interaktif

Prompt-nya `Pilih menu >`. Menu yang tampil:

| Nomor | Perintah | Fungsi |
| --- | --- | --- |
| 1 | `calculate` | enam input: jenis, kondisi, tahun, jumlah pinjaman, tenor, DP |
| 2 | `load` | ambil data dari web service, lalu hitung |
| 3 | `save sheet [nama]` | simpan hasil terakhir |
| 4 | `switch sheet [id/nama]` | pindah ke sheet yang sudah disimpan |
| 5 | `clear` | bersihkan layar, lalu tampilkan menu lagi |
| 6 | `exit` | keluar |

`show` tetap bisa diketik. Di mode file perintah itu mencetak daftar perintah. Di mode interaktif layar dibersihkan dulu supaya daftar tidak menumpuk.

Bisa juga menempel satu baris:

```text
Mobil, Baru, 2025, 1000000000, 6, 500000000
```

Kalau baris pertama yang diketik `Mobil` atau `Motor`, aplikasi langsung lanjut ke input berikutnya.

## Mode file

Tiap baris di file diperlakukan sebagai perintah. Baris kosong dan baris yang diawali `#` dilewati. Contohnya ada di `file_inputs.txt`.

## Test

```text
mvn test
```

## Docker

```text
docker build -t credit-simulator:1.0 .
docker run -it credit-simulator:1.0
docker run -it credit-simulator:1.0 file_inputs.txt
```

Workflow CI (`.github/workflows/ci.yml`) menjalankan test, kedua skrip, dan `docker build`. Push ke Docker Hub terjadi kalau repository punya secret `DOCKERHUB_USERNAME` dan `DOCKERHUB_TOKEN`.

Input yang tidak sesuai (jenis salah, tahun bukan 4 digit, pinjaman atau DP negatif, tenor di luar 1-6, DP di bawah minimum) ditolak dengan pesan. Aplikasi tidak berhenti karena satu baris yang salah.
