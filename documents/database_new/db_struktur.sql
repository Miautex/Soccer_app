-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 13. Jun 2017 um 21:47
-- Server-Version: 5.7.17-0ubuntu0.16.04.1
-- PHP-Version: 7.0.15-0ubuntu0.16.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `SOCCER_APP_4BG2`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `games`
--

CREATE TABLE `games` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL,
  `remark` varchar(2500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scoreTeamA` int(11) NOT NULL,
  `scoreTeamB` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `participation`
--

CREATE TABLE `participation` (
  `id` int(11) NOT NULL,
  `idGame` int(11) NOT NULL,
  `idPlayer` int(11) NOT NULL,
  `goalsGot` int(11) NOT NULL,
  `goalsShotDefault` int(11) NOT NULL,
  `goalsShotHead` int(11) NOT NULL,
  `goalsShotHeadSnow` int(11) NOT NULL,
  `goalsShotPenalty` int(11) NOT NULL,
  `nutmeg` int(11) NOT NULL,
  `team` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `position` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `players`
--

CREATE TABLE `players` (
  `ID` int(10) NOT NULL,
  `USERNAME` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `NAME` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ISADMIN` tinyint(1) NOT NULL DEFAULT '0',
  `PASSWORD` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ISACTIVE` tinyint(1) NOT NULL,
  `loginkey` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `location` point DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `playersGamePositions`
--

CREATE TABLE `playersGamePositions` (
  `id_player` int(11) NOT NULL,
  `position` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `positions`
--

CREATE TABLE `positions` (
  `NAME` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `teams`
--

CREATE TABLE `teams` (
  `NAME` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `games`
--
ALTER TABLE `games`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `participation`
--
ALTER TABLE `participation`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idGame_2` (`idGame`,`idPlayer`),
  ADD KEY `idGame` (`idGame`),
  ADD KEY `idPlayer` (`idPlayer`),
  ADD KEY `team` (`team`),
  ADD KEY `position` (`position`);

--
-- Indizes für die Tabelle `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `USERNAME` (`USERNAME`);

--
-- Indizes für die Tabelle `playersGamePositions`
--
ALTER TABLE `playersGamePositions`
  ADD PRIMARY KEY (`id_player`,`position`(1)),
  ADD KEY `position` (`position`);

--
-- Indizes für die Tabelle `positions`
--
ALTER TABLE `positions`
  ADD PRIMARY KEY (`NAME`);

--
-- Indizes für die Tabelle `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`NAME`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `games`
--
ALTER TABLE `games`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;
--
-- AUTO_INCREMENT für Tabelle `participation`
--
ALTER TABLE `participation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=274;
--
-- AUTO_INCREMENT für Tabelle `players`
--
ALTER TABLE `players`
  MODIFY `ID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=148;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `participation`
--
ALTER TABLE `participation`
  ADD CONSTRAINT `participation_ibfk_1` FOREIGN KEY (`idGame`) REFERENCES `games` (`id`),
  ADD CONSTRAINT `participation_ibfk_2` FOREIGN KEY (`idPlayer`) REFERENCES `players` (`ID`),
  ADD CONSTRAINT `participation_ibfk_3` FOREIGN KEY (`position`) REFERENCES `positions` (`NAME`),
  ADD CONSTRAINT `participation_ibfk_4` FOREIGN KEY (`team`) REFERENCES `teams` (`NAME`);

--
-- Constraints der Tabelle `playersGamePositions`
--
ALTER TABLE `playersGamePositions`
  ADD CONSTRAINT `playersGamePositions_ibfk_1` FOREIGN KEY (`id_player`) REFERENCES `players` (`ID`),
  ADD CONSTRAINT `playersGamePositions_ibfk_2` FOREIGN KEY (`position`) REFERENCES `positions` (`NAME`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
