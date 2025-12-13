import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, ScrollView, Alert } from 'react-native';
import { useDispatch } from 'react-redux';
import { createPatient } from '../store/slices/patientSlice';
import { COLORS, GENDER_OPTIONS, BLOOD_GROUP_OPTIONS, RELATIONSHIP_OPTIONS } from '../constants';

const AddPatientScreen = ({ navigation }: any) => {
  const [name, setName] = useState('');
  const [dob, setDob] = useState('');
  const [gender, setGender] = useState('MALE');
  const [bloodGroup, setBloodGroup] = useState('');
  const [allergies, setAllergies] = useState('');
  const [relationship, setRelationship] = useState('Self');

  const dispatch = useDispatch();

  const handleSubmit = async () => {
    if (!name || !dob) {
      Alert.alert('Error', 'Please fill in required fields (Name and DOB)');
      return;
    }

    try {
      await dispatch(createPatient({
        name,
        dob,
        gender,
        bloodGroup,
        allergies,
        relationship,
      }) as any).unwrap();

      Alert.alert('Success', 'Patient added successfully');
      navigation.goBack();
    } catch (error: any) {
      Alert.alert('Error', error || 'Failed to add patient');
    }
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.form}>
        <Text style={styles.label}>Full Name *</Text>
        <TextInput
          style={styles.input}
          value={name}
          onChangeText={setName}
          placeholder="Enter full name"
        />

        <Text style={styles.label}>Date of Birth * (YYYY-MM-DD)</Text>
        <TextInput
          style={styles.input}
          value={dob}
          onChangeText={setDob}
          placeholder="1990-01-01"
        />

        <Text style={styles.label}>Gender</Text>
        <View style={styles.radioGroup}>
          {GENDER_OPTIONS.map((option) => (
            <TouchableOpacity
              key={option.value}
              style={[
                styles.radioButton,
                gender === option.value && styles.radioButtonSelected,
              ]}
              onPress={() => setGender(option.value)}
            >
              <Text
                style={[
                  styles.radioText,
                  gender === option.value && styles.radioTextSelected,
                ]}
              >
                {option.label}
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        <Text style={styles.label}>Blood Group</Text>
        <View style={styles.bloodGroupContainer}>
          {BLOOD_GROUP_OPTIONS.map((bg) => (
            <TouchableOpacity
              key={bg}
              style={[
                styles.bloodGroupButton,
                bloodGroup === bg && styles.bloodGroupButtonSelected,
              ]}
              onPress={() => setBloodGroup(bg)}
            >
              <Text
                style={[
                  styles.bloodGroupText,
                  bloodGroup === bg && styles.bloodGroupTextSelected,
                ]}
              >
                {bg}
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        <Text style={styles.label}>Relationship</Text>
        <View style={styles.relationshipContainer}>
          {RELATIONSHIP_OPTIONS.map((rel) => (
            <TouchableOpacity
              key={rel}
              style={[
                styles.relationshipButton,
                relationship === rel && styles.relationshipButtonSelected,
              ]}
              onPress={() => setRelationship(rel)}
            >
              <Text
                style={[
                  styles.relationshipText,
                  relationship === rel && styles.relationshipTextSelected,
                ]}
              >
                {rel}
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        <Text style={styles.label}>Allergies (Optional)</Text>
        <TextInput
          style={[styles.input, styles.textArea]}
          value={allergies}
          onChangeText={setAllergies}
          placeholder="Enter any known allergies"
          multiline
          numberOfLines={3}
        />

        <TouchableOpacity style={styles.button} onPress={handleSubmit}>
          <Text style={styles.buttonText}>Add Patient</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  form: {
    padding: 16,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    marginTop: 16,
    marginBottom: 8,
    color: COLORS.black,
  },
  input: {
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
    backgroundColor: COLORS.white,
  },
  textArea: {
    height: 80,
    textAlignVertical: 'top',
  },
  radioGroup: {
    flexDirection: 'row',
    gap: 8,
  },
  radioButton: {
    flex: 1,
    padding: 12,
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 8,
    alignItems: 'center',
    backgroundColor: COLORS.white,
  },
  radioButtonSelected: {
    backgroundColor: COLORS.primary,
    borderColor: COLORS.primary,
  },
  radioText: {
    color: COLORS.black,
  },
  radioTextSelected: {
    color: COLORS.white,
    fontWeight: 'bold',
  },
  bloodGroupContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  bloodGroupButton: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 8,
    backgroundColor: COLORS.white,
  },
  bloodGroupButtonSelected: {
    backgroundColor: COLORS.error,
    borderColor: COLORS.error,
  },
  bloodGroupText: {
    color: COLORS.black,
  },
  bloodGroupTextSelected: {
    color: COLORS.white,
    fontWeight: 'bold',
  },
  relationshipContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  relationshipButton: {
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 8,
    backgroundColor: COLORS.white,
  },
  relationshipButtonSelected: {
    backgroundColor: COLORS.secondary,
    borderColor: COLORS.secondary,
  },
  relationshipText: {
    color: COLORS.black,
  },
  relationshipTextSelected: {
    color: COLORS.white,
    fontWeight: 'bold',
  },
  button: {
    backgroundColor: COLORS.success,
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 24,
    marginBottom: 32,
  },
  buttonText: {
    color: COLORS.white,
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default AddPatientScreen;
