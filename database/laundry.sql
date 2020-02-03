-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 03 Feb 2020 pada 07.57
-- Versi Server: 10.1.30-MariaDB
-- PHP Version: 7.2.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `laundry`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `passwordOne` varchar(64) NOT NULL,
  `passwordTwo` varchar(64) NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `username`, `passwordOne`, `passwordTwo`, `createdAt`) VALUES
(1, 'IndraSudirman', 'bfcc83ec6203141323a6e2f392edb73d', '9d54178d6411bb0c0f7c787a8304df0b', '2020-01-27 15:08:55'),
(2, 'DellaPanji', 'e75df1afc3490385a6fdd1d5eba0e71c', '4284a4a66a37ae35934bfda3604ca3f3', '2020-02-03 12:45:44'),
(3, 'MIqbal', '03836a43e1ac136bdd3b075d89017ec4', 'f54fd6e10a187d2da0d0de997aa68f12', '2020-02-03 13:06:28'),
(4, 'MilaSari', 'cbb37d09cfc2fc410adf0dfd6d9c06ca', 'be0aa1002e63aa01c5144ebac6c08d9b', '2020-02-03 13:16:42'),
(5, 'YunitaS', '53cefb7c390d2b7b200488f1e6cef797', '71fbee2cc2c4e4c854ec14d900a643bc', '2020-02-03 13:34:43'),
(6, 'PajriA', 'c102cd404da336e030c400b68dbc0d21', '8a7576469225c70af1b3170daef79444', '2020-02-03 13:41:47'),
(7, 'LailulHuda', 'cd7a6b5dbd54bbaf0c64d61516c0b46f', 'ffb5c24b7d25490d9b9418d530a17778', '2020-02-03 13:47:21');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
