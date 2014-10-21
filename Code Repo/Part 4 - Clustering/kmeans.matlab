%% generate sample data
K = 30;
%%numObservarations = 41104;
%%dimensions = 32926;
%%data = rand([numObservarations dimensions]);
data = csvread('/Users/matthewletter/Documents/Challenge1/matLab/vectorMatrix.csv');

%% cluster
% % opts = statset('MaxIter', 30, 'Display', 'iter');
% % [clustIDX, clusters, interClustSum, Dist] = kmeans(data, K,'options',opts, ...
% % 'distance','sqEuclidean', 'EmptyAction','singleton', 'replicates',3);
opts = statset('Display', 'iter');
%%
b = mean([data(1:2:end),data(2:2:end)],2);
%%
D = pdist(data,'jaccard');
%%
[Y, eigvals] = cmdscale(B);
%%
[idx,C] = kmeans(data, 30,'options',opts);
%%

%% plot data+clusters
figure, hold on
scatter3(data(:,1),data(:,2),data(:,3), 50, clustIDX, 'filled')
scatter3(clusters(:,1),clusters(:,2),clusters(:,3), 200, (1:K)', 'filled')
hold off, xlabel('x'), ylabel('y'), zlabel('z')

%% plot clusters quality
figure
[silh,h] = silhouette(data, clustIDX);
avrgScore = mean(silh);


%% Assign data to clusters
% calculate distance (squared) of all instances to each cluster centroid
D = zeros(numObservarations, K);     % init distances
for k=1:K
    %d = sum((x-y).^2).^0.5
    D(:,k) = sum( ((data - repmat(clusters(k,:),numObservarations,1)).^2), 2);
end

% find  for all instances the cluster closet to it
[minDists, clusterIndices] = min(D, [], 2);

% compare it with what you expect it to be
sum(clusterIndices == clustIDX)