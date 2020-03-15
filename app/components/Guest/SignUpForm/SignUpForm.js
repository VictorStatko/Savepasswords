import React, {Component} from 'react';
import FormUserDetails from "./FormUserDetails";
import styles from "./SignUpForm.module.scss";
import {connect} from "react-redux";
import {trySignUp} from "ducks/account/actions";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import {Link} from "react-router-dom";
import {toast} from 'react-toastify';
import i18n from "i18n";
import history from "utils/history";
import {
    encryptPrivateKeyToPemString,
    generateSalt,
    getRSAKeyPair,
    publicCryptoKeyToPemString
} from "utils/encryptionUtils";
import argon2 from "argon2-browser";
import {reverseString} from "utils/stringUtils";

class SignUpForm extends Component {
    state = {
        email: "",
        password: ""
    };

    handleChange = (input, value) => {
        this.setState({[input]: value});
    };

    /*  onSubmit = async e => {
          e.preventDefault();
          const clientPasswordSalt = generateSalt(16);
          const keypair = await getRSAKeyPair(2048, 'SHA-512', 'RSA-OAEP', ['encrypt', 'decrypt'], true);
          let clientPasswordHash;

          try {
              clientPasswordHash = await argon2.hash(
                  {
                      pass: this.state.password,
                      salt: clientPasswordSalt
                  }
              );

              const wrappedPrivateKey = await encryptPrivateKeyToPemString(
                  keypair.privateKey,
                  reverseString(this.state.password).trim().normalize(),
                  100000,
                  'SHA-512',
                  'AES-GCM',
                  256
              );
              console.log('wrappedPrivateKey');
              console.log(wrappedPrivateKey);

              const hash = {};
              hash.name = 'SHA-512';

              const decryptedPrivateKey = await decryptPrivateKeyFromPemString(
                  wrappedPrivateKey,
                  reverseString(this.state.password).trim().normalize(),
                  {name: 'RSA-OAEP', isExtractable: false, hash: hash}
              );

              console.log('decryptedPrivateKey');
              console.log(decryptedPrivateKey);

              const publicKeyString = await publicCryptoKeyToPemString(keypair.publicKey);

              console.log('publicKeyString');
              console.log(publicKeyString);

              const publicKeyCryptoFromString = await pemStringToPublicCryptoKey(
                  publicKeyString,
                  {
                      isExtractable: false,
                      name: 'RSA-OAEP',
                      hash: 'SHA-512',
                      usage: '[\'encrypt\', wrapKey]'
                  }
              );

              console.log('publicKeyCryptoFromString');
              console.log(publicKeyCryptoFromString);
          } catch (e) {
              console.error(e);
              toast.error(i18n.t('exceptions.clientEncryptionError'));
              return;
          }

          /!* await this.props.trySignUp({
               email: this.state.email,
               password: clientPasswordHash.encoded,
               clientPasswordSalt: clientPasswordSalt
           });

           toast.success(i18n.t('signUp.success'));
           history.push('/sign-in');*!/
      };*/

    onSubmit = async e => {
        e.preventDefault();
        const passwordSalt = generateSalt(16);
        let passwordHash;
        let RSAKeyPair;
        let encryptedPrivateKeyPemString;
        let publicKeyPemString;

        try {
            passwordHash = await argon2.hash(
                {
                    pass: this.state.password,
                    salt: passwordSalt
                }
            );

            RSAKeyPair = await getRSAKeyPair(2048, 'SHA-512', 'RSA-OAEP', ['encrypt', 'decrypt'], true);

            encryptedPrivateKeyPemString = await encryptPrivateKeyToPemString(
                RSAKeyPair.privateKey,
                reverseString(this.state.password).trim().normalize(),
                100000,
                'SHA-512',
                'AES-GCM',
                256
            );

            publicKeyPemString = await publicCryptoKeyToPemString(RSAKeyPair.publicKey);
        } catch (e) {
            console.error(e);
            toast.error(i18n.t('exceptions.clientEncryptionError'));
            return;
        }

        await this.props.trySignUp({
            email: this.state.email,
            password: passwordHash.encoded,
            clientPasswordSalt: passwordSalt,
            privateKey: encryptedPrivateKeyPemString,
            publicKey: publicKeyPemString
        });

        toast.success(i18n.t('signUp.success'));
        history.push('/sign-in');
    };

    render() {
        const {t} = this.props;

        return (
            <React.Fragment>
                <form onSubmit={this.onSubmit}>
                    <FormUserDetails
                        handleChange={this.handleChange}
                        password={this.state.password}
                        email={this.state.email}
                    />
                </form>
                <div className={styles.changePageLink}>
                    <Link to={'/sign-in'}>{t('signUp.alreadyRegisteredLink')}</Link>
                </div>
            </React.Fragment>
        )
    }
}

SignUpForm.propTypes = {};

const mapStateToProps = (state) => {
    return {
        account: state.account
    }
};

const withConnect = connect(
    mapStateToProps,
    {
        trySignUp: trySignUp
    }
);

export default compose(withTranslation(), withConnect)(SignUpForm);
