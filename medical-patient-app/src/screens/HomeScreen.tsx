import React, { useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, FlatList, ScrollView } from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { fetchPatients } from '../store/slices/patientSlice';
import { RootState } from '../store';
import { COLORS } from '../constants';

const HomeScreen = ({ navigation }: any) => {
  const dispatch = useDispatch();
  const { user } = useSelector((state: RootState) => state.auth);
  const { patients } = useSelector((state: RootState) => state.patient);

  useEffect(() => {
    dispatch(fetchPatients() as any);
  }, []);

  const dashboardCards = [
    { title: '📷 Scan Prescription', route: 'ScanPrescription', color: COLORS.primary },
    { title: '📤 Upload Image', route: 'UploadPrescription', color: COLORS.secondary },
    { title: '🗂 Medical History', route: 'MedicalHistory', color: COLORS.success },
    { title: '⚠ Health Alerts', route: 'Alerts', color: COLORS.warning },
  ];

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.welcome}>Welcome, {user?.name}!</Text>
        <TouchableOpacity 
          style={styles.patientSelector}
          onPress={() => navigation.navigate('PatientList')}
        >
          <Text style={styles.patientText}>
            {patients.length} Patient(s) •  Tap to manage
          </Text>
        </TouchableOpacity>
      </View>

      <View style={styles.cardsContainer}>
        {dashboardCards.map((card, index) => (
          <TouchableOpacity
            key={index}
            style={[styles.card, { backgroundColor: card.color }]}
            onPress={() => navigation.navigate(card.route)}
          >
            <Text style={styles.cardTitle}>{card.title}</Text>
          </TouchableOpacity>
        ))}
      </View>

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Recent Patients</Text>
        {patients.slice(0, 3).map((patient) => (
          <TouchableOpacity
            key={patient.id}
            style={styles.patientItem}
            onPress={() => {
              navigation.navigate('PatientDetails', { patientId: patient.id });
            }}
          >
            <Text style={styles.patientName}>{patient.name}</Text>
            <Text style={styles.patientInfo}>
              {patient.age} years • {patient.bloodGroup || 'N/A'}
            </Text>
          </TouchableOpacity>
        ))}
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  header: {
    padding: 20,
    backgroundColor: COLORS.white,
  },
  welcome: {
    fontSize: 24,
    fontWeight: 'bold',
    color: COLORS.black,
    marginBottom: 8,
  },
  patientSelector: {
    backgroundColor: COLORS.lightGray,
    padding: 12,
    borderRadius: 8,
  },
  patientText: {
    color: COLORS.black,
    textAlign: 'center',
  },
  cardsContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    padding: 10,
  },
  card: {
    width: '48%',
    height: 120,
    margin: '1%',
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 16,
  },
  cardTitle: {
    color: COLORS.white,
    fontSize: 16,
    fontWeight: 'bold',
    textAlign: 'center',
  },
  section: {
    backgroundColor: COLORS.white,
    margin: 10,
    padding: 16,
    borderRadius: 12,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 12,
  },
  patientItem: {
    padding: 12,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  patientName: {
    fontSize: 16,
    fontWeight: '600',
  },
  patientInfo: {
    fontSize: 14,
    color: COLORS.gray,
    marginTop: 4,
  },
});

export default HomeScreen;
