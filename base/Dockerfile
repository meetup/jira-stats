FROM openjdk:8u121-jdk

# Install google cloud tools
RUN wget https://dl.google.com/dl/cloudsdk/channels/rapid/google-cloud-sdk.tar.gz && \
  tar -xzvf google-cloud-sdk.tar.gz && rm -rf google-cloud-sdk.tar.gz && \
  google-cloud-sdk/install.sh \
    --usage-reporting false \
    --path-update true \
    --command-completion true

ENV PATH="/google-cloud-sdk/bin:${PATH}"

ARG CI_BUILD_NUMBER
ENV CI_BUILD_NUMBER=${CI_BUILD_NUMBER}

ADD opt /opt
