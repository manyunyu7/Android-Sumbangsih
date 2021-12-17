package com.feylabs.sumbangsih.data.source.local

import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse

data class NewsLocal(
    val title: String,
    val image: String,
    val content: String,
    val date: String,
    val author: String
)

object NewsSeeder {
    fun getNewsSeeder(): MutableList<NewsResponse.NewsResponseItem> {
        val list = mutableListOf<NewsResponse.NewsResponseItem>()

        list.add(
            NewsResponse.NewsResponseItem(
                title = "Pemerintah Tambah Kuota Penerima BPUM Jadi 12,8 Juta UMKM",
                author = "Shizi Manyunyu",
                dateIndo = "Rabu, 05 Mei 2021",
                photoPath = "https://blogpictures.99.co/iStock-1226057251.png",
                content = "Jakarta - Pemerintah berencana memperluas cakupan pemberian Banpres Produktif Usaha Mikro (BPUM) menjadi 12,8 juta pelaku UMKM yang terdampak pandemi COVID-19 pada 2021. Anggaran yang disiapkan mencapai Rp 15,36 triliun dengan skema setiap UMKM mendapatkan dana Rp 1,2 juta.\n" +
                        "\n" +
                        "\"Direncanakan akan (ada) 12,8 juta penerima yang merupakan kombinasi dari penerima manfaat lama dan baru,\" ujar Deputi Bidang Usaha Mikro Kemenkop UKM Eddy Satriya dikutip dalam keterangan tertulis, Rabu (5/5/2021).\n" +
                        "\n" +
                        "Menurut Eddy, anggaran yang ada untuk saat ini, Rp 11,76 triliun baru dapat menjangkau 9,8 juta penerima manfaat. Saat ini Kemenkop UKM sudah menyalurkan ke 8,6 juta penerima atau sebanyak Rp 10,4 triliun (88%). Setelah menyentuh angka 9 juta, rencananya Kemenkop UKM akan menambah lagi 3 juta penerima manfaat BPUM ini.\n" +
                        "\n" +
                        "\"Pemerintah dalam hal ini Kemenkop UKM dan Kemenko Perekonomian terus mendampingi UMKM termasuk dalam hal pembiayaan untuk naik kelas, mulai dari kredit usaha rakyat (KUR), Super Mikro, sampai Mikro,\" ungkap Eddy.\n" +
                        "\n" +
                        "Asisten Deputi Koperasi & UKM Kemenko Perekonomian Iwan Faidi menimpali, BPS mencatat ada ratusan ribu orang yang membuka usaha baru semenjak pandemi, dan mereka menyerap tenaga kerja dari usahanya.\n" +
                        "\n" +
                        "\"BPUM ini memang memberikan efek luar biasa, hal ini dibuktikan oleh data BPS yang menunjukkan penambahan sekitar 760 ribu orang yang menjalankan usaha baru, dan buruh informal naik 4,5 juta pekerja,\" urai Iwan.\n" +
                        "\n" +
                        "Jwan menerangkan program BPUM dibuat agar UMKM dapat terus mempertahankan usahanya. Dengan begitu potensi pengurangan tenaga kerja juga dapat ditekan.\n" +
                        "\n" +
                        "\"Selain BPUM, ada insentif lainnya seperti subsidi bunga, penempatan dana pemerintah pada bank umum, penjaminan kredit modal kerja melalui imbal jasa penjaminan, PPh final bagi UMKM yang ditanggung pemerintah. Untuk 2021 dianggarkan Rp 181,9 triliun untuk insentif bagi UMKM Indonesia,\" papar Iwan."
                )
        )

        list.add(
            NewsResponse.NewsResponseItem(
                title = "Asyik! Penyaluran BLT UMKM Rp 1,2 Juta Dikebut Sampai Lebaran",
                author = "Anisa Indraini",
                dateIndo = "Rabu, 05 Mei 2021",
                photoPath = "https://pasardana.id/media/12911/bansos-non-tunai-istimewa.jpg?crop=0,0,0,0.15555555555555561&cropmode=percentage&width=675&height=380&rnd=132532013780000000",
                content = "Jakarta - Menteri Koperasi dan UKM Teten Masduki memastikan akan mempercepat penyaluran Bantuan Produktif Usaha Mikro (BPUM) atau BLT UMKM Rp 1,2 juta. Bantuan akan terus dipercepat sampai Lebaran.\n" +
                        "\"Kami akan terus kejar sampai Lebaran karena ini saya kira berkaitan juga untuk kemampuan mendorong daya beli di masyarakat,\" kata Teten dalam konferensi pers virtual tentang Perkembangan & Upaya Pemulihan Ekonomi Nasional, Rabu (5/5/2021).\n" +
                        "\n" +
                        "Berdasarkan datanya, realisasi BLT UMKM terbaru sudah diberikan kepada 8.635.025 penerima atau 88,11% dari target kuartal I-2021. Total dana yang disalurkan sebesar Rp 10,4 triliun.\n" +
                        "\n" +
                        "\"Realisasi Banpres Produktif untuk usaha mikro saat ini dari anggaran Rp 12,36 triliun untuk 12,8 (juta) pelaku usaha di target awal untuk penyaluran 9,8 juta ini sudah terealisir 88,11% atau 8,6 juta pelaku usaha mikro,\" tuturnya.\n" +
                        "\n" +
                        "Penasaran kamu termasuk penerima BLT UMKM Rp 1,2 juta atau bukan? Coba cek di eform.bri.co.id/bpum. Ikuti caranya, seperti berikut ini:\n" +
                        "1. Klik e-form BRI (https://eform.bri.co.id/bpum)\n" +
                        "2. Masukkan nomor KTP dan kode verifikasi\n" +
                        "3. Klik proses inquiry\n" +
                        "4. Jika sudah masuk, Anda akan menerima pemberitahuan apakah sudah mendapatkan bantuan atau tidak\n" +
                        "5. Jika terdaftar sebagai penerima, kamu sebagai pelaku usaha mikro bisa mendatangi kantor BRI untuk mencairkan BLT UMKM Rp 1,2 juta. Bantuan juga bisa langsung ditransfer ke rekening.\n" +
                        "\n" +
                        "Selain BLT UMKM, bantuan juga diberikan berupa program Kredit Usaha Rakyat (KUR). Pemerintah sendiri telah menaikkan plafon KUR dari Rp 50 juta menjadi Rp 100 juta dengan tingkat suku bunga menjadi 3%. Program itu diperpanjang dan berlaku hingga Desember 2021.\n" +
                        "\n" +
                        "\"Tahun 2021 pemerintah juga memberikan dukungan akses pembiayaan untuk UMKM melalui program KUR dengan target Rp 250 triliun dan ini berpotensi ditingkatkan jadi Rp 285 triliun. Relaksasi subsidi bunga tambahan 3%, jadi suku bunga KUR sampai akhir Desember sebesar 3%,\" jelasnya."
            )
        )

        return list
    }
}

