<template>
  <div>
    <div class="row">
      <div class="col">
        <h5>Settings</h5>
      </div>
    </div>
    <!-- /.row -->

    <div class="row">
      <div class="col">
        <b-alert variant="danger" dismissible :show="errorMessage!==''" @dismissed="errorMessage=''">
          {{ errorMessage }}
        </b-alert>
      </div>
    </div>
    <!-- /.row -->

    <div class="row">
      <div class="col">
        <h6>Push notifications</h6>
        <p>You can enable or disable push notifications for each camera. If enabled, a push notification will be
          sent as soon as a new image is received via FTP.</p>
      </div>
    </div>
    <!-- /.row -->

    <div class="row pb-1" v-for="setting in settings">
      <div class="col col-lg-2">{{ setting.cameraName }}</div>
      <div class="col col-lg-10">
        <div class="btn-group">
          <b-button size="sm"
             :pressed="setting.enabled"
             :variant="setting.enabled ? 'primary' : 'secondary'"
             v-on:click="toggleSetting(setting.cameraId)">ON</b-button>
          <b-button size="sm"
             :pressed="!setting.enabled"
             :variant="!setting.enabled ? 'primary' : 'secondary'"
             v-on:click="toggleSetting(setting.cameraId)">OFF</b-button>
        </div>
      </div>
    </div>
    <!-- /.row -->

  </div>
</template>

<script>
import api from '../services/api';

export default {
  name: 'Settings',

  created() {
    this.fetchSettings();
  },

  data() {
    return {
      settings: [],
      errorMessage: '',
    };
  },

  methods: {

    /**
     * Fetch settings data from REST API.
     */
    fetchSettings() {
      api().get('push-notification-settings')
        .then((response) => {
          this.settings = response.data;
        })
        .catch((error) => {
          this.errorMessage = error.message;
        });
    },

    /**
     * Callback if settings toggle has been clicked.
     * Push new state to REST endpoint.
     */
    toggleSetting(cameraId) {
      const currentSetting = this.settings.find(setting => setting.cameraId === cameraId);

      const data = {
        cameraId: currentSetting.cameraId,
        enabled: !currentSetting.enabled,
      };

      api().patch(`push-notification-settings/${cameraId}`, data)
        .then(() => {
          this.fetchSettings();
        })
        .catch((error) => {
          this.errorMessage = error.message;
        });
    },

  },
};
</script>
