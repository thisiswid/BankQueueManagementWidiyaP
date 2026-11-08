public class AntrianBank {

    static class Nasabah {
        private final String nama;
        private final String jenis;

        Nasabah(String nama, String jenis) {
            this.nama = nama;
            this.jenis = jenis;
        }

        public String toString() {
            return nama + " (" + jenis + ")";
        }
    }

    // Circular queue menggunakan array biasa
    static class CircularQueue {
        private Nasabah[] data;
        private int front;
        private int rear;
        private int size;

        CircularQueue(int kapasitasAwal) {
            data = new Nasabah[kapasitasAwal];
            front = 0;
            rear = 0;
            size = 0;
        }

        boolean isEmpty() {
            return size == 0;
        }

        int size() {
            return size;
        }

        void enqueue(Nasabah nasabah) {
            if (size == data.length) {
                resize();
            }

            data[rear] = nasabah;
            rear = (rear + 1) % data.length;
            size++;
        }

        Nasabah dequeue() {
            if (isEmpty()) {
                return null;
            }

            Nasabah hasil = data[front];
            data[front] = null;

            front = (front + 1) % data.length;
            size--;

            return hasil;
        }

        // Memperbesar kapasitas array jika penuh
        private void resize() {
            Nasabah[] baru =
                new Nasabah[data.length * 2];

            for (int i = 0; i < size; i++) {
                baru[i] = data[(front + i) % data.length];
            }

            data = baru;
            front = 0;
            rear = size;
        }

        String contents() {
            if (isEmpty()) {
                return "(kosong)";
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    sb.append(" -> ");
                }

                sb.append(
                    data[(front + i) % data.length].nama
                );
            }

            return sb.toString();
        }
    }

    private final CircularQueue vipQueue =
        new CircularQueue(4);

    private final CircularQueue regulerQueue =
        new CircularQueue(4);

    private final int batasVIP;
    private int vipDilayaniBeruntun;

    AntrianBank(int batasVIP) {
        if (batasVIP < 1) {
            throw new IllegalArgumentException(
                "Batas VIP minimal 1"
            );
        }

        this.batasVIP = batasVIP;
        this.vipDilayaniBeruntun = 0;
    }

    // Memasukkan nasabah VIP
    public void enqueueVIP(String nama) {
        vipQueue.enqueue(
            new Nasabah(nama, "VIP")
        );

        System.out.println(
            "Masuk antrean VIP: " + nama
        );
    }

    // Memasukkan nasabah reguler
    public void enqueueReguler(String nama) {
        regulerQueue.enqueue(
            new Nasabah(nama, "Reguler")
        );

        System.out.println(
            "Masuk antrean Reguler: " + nama
        );
    }

    // Mengambil nasabah sesuai aturan pelayanan
    public Nasabah dequeue() {
        Nasabah terpilih;

        if (vipQueue.isEmpty() &&
            regulerQueue.isEmpty()) {

            System.out.println("Antrean kosong.");
            return null;
        }

        if (!regulerQueue.isEmpty() &&
            (vipQueue.isEmpty() ||
             vipDilayaniBeruntun >= batasVIP)) {

            terpilih = regulerQueue.dequeue();
            vipDilayaniBeruntun = 0;

        } else {
            terpilih = vipQueue.dequeue();

            if (terpilih != null) {
                vipDilayaniBeruntun++;
            } else {
                terpilih = regulerQueue.dequeue();
                vipDilayaniBeruntun = 0;
            }
        }

        System.out.println("Dilayani: " + terpilih);

        return terpilih;
    }

    // Menampilkan isi kedua antrean
    public void displayQueue() {
        System.out.println(
            "VIP     : " + vipQueue.contents()
        );

        System.out.println(
            "Reguler : " + regulerQueue.contents()
        );

        System.out.println(
            "Aturan  : maksimal " + batasVIP +
            " VIP berturut-turut jika reguler menunggu."
        );
    }

    public static void main(String[] args) {

        AntrianBank bank = new AntrianBank(2);

        System.out.println(
            "=== SIMULASI ANTREAN BANK ==="
        );

        bank.enqueueVIP("Vina");
        bank.enqueueVIP("Budi");
        bank.enqueueReguler("Rani");
        bank.enqueueVIP("Doni");
        bank.enqueueVIP("Siska");
        bank.enqueueReguler("Agus");
        bank.enqueueVIP("Tono");

        System.out.println("\nKondisi awal:");
        bank.displayQueue();

        System.out.println("\nUrutan pelayanan:");

        while (!bank.vipQueue.isEmpty() ||
               !bank.regulerQueue.isEmpty()) {

            bank.dequeue();
        }

        System.out.println("\nKondisi akhir:");
        bank.displayQueue();
    }
}
