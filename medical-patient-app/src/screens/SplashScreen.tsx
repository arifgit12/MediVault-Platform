import React, { useEffect } from 'react';
import { View, Text, ActivityIndicator, StyleSheet } from 'react-native';
import { useDispatch } from 'react-redux';
import { loadStoredAuth } from '../store/slices/authSlice';
import { COLORS } from '../constants';

const SplashScreen = ({ navigation }: any) => {
  const dispatch = useDispatch();

  useEffect(() => {
    checkAuth();
  }, []);

  const checkAuth = async () => {
    try {
      const result = await dispatch(loadStoredAuth() as any);
      setTimeout(() => {
        if (result.payload) {
          navigation.replace('Home');
        } else {
          navigation.replace('Login');
        }
      }, 1000);
    } catch (error) {
      navigation.replace('Login');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>MediVault</Text>
      <Text style={styles.subtitle}>Your Medical Records, Secure & Accessible</Text>
      <ActivityIndicator size="large" color={COLORS.primary} style={styles.loader} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: COLORS.primary,
  },
  title: {
    fontSize: 48,
    fontWeight: 'bold',
    color: COLORS.white,
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: COLORS.white,
    marginBottom: 32,
  },
  loader: {
    marginTop: 20,
  },
});

export default SplashScreen;
