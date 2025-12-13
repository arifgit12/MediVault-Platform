import React, { useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, FlatList } from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { fetchPatients } from '../store/slices/patientSlice';
import { RootState } from '../store';
import { COLORS } from '../constants';

const PatientListScreen = ({ navigation }: any) => {
  const dispatch = useDispatch();
  const { patients, loading } = useSelector((state: RootState) => state.patient);

  useEffect(() => {
    dispatch(fetchPatients() as any);
  }, []);

  return (
    <View style={styles.container}>
      <FlatList
        data={patients}
        keyExtractor={(item) => item.id.toString()}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.patientCard}
            onPress={() => navigation.navigate('PatientDetails', { patientId: item.id })}
          >
            <View style={styles.patientInfo}>
              <Text style={styles.patientName}>{item.name}</Text>
              <Text style={styles.patientDetails}>
                {item.age} years • {item.gender} • {item.bloodGroup || 'N/A'}
              </Text>
              <Text style={styles.relationship}>{item.relationship || 'Self'}</Text>
            </View>
            <TouchableOpacity
              style={styles.editButton}
              onPress={() => navigation.navigate('EditPatient', { patient: item })}
            >
              <Text style={styles.editText}>Edit</Text>
            </TouchableOpacity>
          </TouchableOpacity>
        )}
      />

      <TouchableOpacity
        style={styles.fab}
        onPress={() => navigation.navigate('AddPatient')}
      >
        <Text style={styles.fabText}>+</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  patientCard: {
    backgroundColor: COLORS.white,
    padding: 16,
    marginHorizontal: 16,
    marginVertical: 8,
    borderRadius: 12,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  patientInfo: {
    flex: 1,
  },
  patientName: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 4,
  },
  patientDetails: {
    fontSize: 14,
    color: COLORS.gray,
    marginBottom: 4,
  },
  relationship: {
    fontSize: 12,
    color: COLORS.primary,
  },
  editButton: {
    backgroundColor: COLORS.primary,
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 8,
  },
  editText: {
    color: COLORS.white,
    fontWeight: 'bold',
  },
  fab: {
    position: 'absolute',
    right: 20,
    bottom: 20,
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: COLORS.primary,
    justifyContent: 'center',
    alignItems: 'center',
    elevation: 4,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 4,
  },
  fabText: {
    fontSize: 32,
    color: COLORS.white,
  },
});

export default PatientListScreen;
