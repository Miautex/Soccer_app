-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 31. Mrz 2017 um 12:58
-- Server-Version: 5.7.17-0ubuntu0.16.04.1
-- PHP-Version: 7.0.15-0ubuntu0.16.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `4BHIFS_2017_SOCCER_G2`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `games`
--

CREATE TABLE `games` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL,
  `remark` varchar(250) NOT NULL,
  `scoreTeamA` int(11) NOT NULL,
  `scoreTeamB` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `games`
--

INSERT INTO `games` (`id`, `date`, `remark`, `scoreTeamA`, `scoreTeamB`) VALUES
(1, '2017-03-16', '', 0, 0),
(2, '2017-03-15', '', 0, 0),
(3, '2017-03-14', '', 0, 0),
(4, '2017-03-12', '', 0, 0);

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
  `team` varchar(20) NOT NULL,
  `position` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `participation`
--

INSERT INTO `participation` (`id`, `idGame`, `idPlayer`, `goalsGot`, `goalsShotDefault`, `goalsShotHead`, `goalsShotHeadSnow`, `goalsShotPenalty`, `nutmeg`, `team`, `position`) VALUES
(5, 1, 1, 0, 0, 0, 0, 0, 0, 'TEAM A', 'ATTACK');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `players`
--

CREATE TABLE `players` (
  `ID` int(10) NOT NULL,
  `USERNAME` varchar(50) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `ISADMIN` tinyint(1) NOT NULL DEFAULT '0',
  `PASSWORD` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `players`
--

INSERT INTO `players` (`ID`, `USERNAME`, `NAME`, `ISADMIN`, `PASSWORD`) VALUES
(1, 'martin', 'Martin', 0, 'a6444a246547facadfbc56ed7a940b7f'),
(2, 'elias', 'Elias', 0, 'b0c5fe61c99681f127d708165717adcf'),
(3, 'marco', 'Marco', 1, 'ef51e2c67b33239dd3c01077cb70008d'),
(4, 'raphael', 'Raphael', 1, 'e01c91e255651ddcbaa452da17dfb87c'),
(5, 'pascal', 'Pascal', 0, 'effd2a3792d289e2dae4490d8b71c1c7'),
(6, 'jakob', 'Jakob', 0, '46b92d9fc6d4824efdac9c329feaf7da'),
(7, 'stefan', 'Stefan', 0, 'f242543cbbdaeb0b618011bd03701d10'),
(8, 'lukas', 'Lukas', 0, 'd3e293c8c3ec9410aa418ce6cecceb04');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `playersGamePositions`
--

CREATE TABLE `playersGamePositions` (
  `id_player` int(11) NOT NULL,
  `position` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `playersGamePositions`
--

INSERT INTO `playersGamePositions` (`id_player`, `position`) VALUES
(4, 'ATTACK'),
(8, 'ATTACK'),
(3, 'DEFENSE'),
(7, 'DEFENSE'),
(8, 'DEFENSE'),
(1, 'GOAL'),
(5, 'GOAL'),
(2, 'MIDFIELD'),
(6, 'MIDFIELD'),
(7, 'MIDFIELD');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `positions`
--

CREATE TABLE `positions` (
  `NAME` varchar(20) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `positions`
--

INSERT INTO `positions` (`NAME`) VALUES
('ATTACK'),
('DEFENSE'),
('GOAL'),
('MIDFIELD');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `teams`
--

CREATE TABLE `teams` (
  `NAME` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `teams`
--

INSERT INTO `teams` (`NAME`) VALUES
('TEAM A'),
('TEAM B');

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT für Tabelle `participation`
--
ALTER TABLE `participation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT für Tabelle `players`
--
ALTER TABLE `players`
  MODIFY `ID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
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
